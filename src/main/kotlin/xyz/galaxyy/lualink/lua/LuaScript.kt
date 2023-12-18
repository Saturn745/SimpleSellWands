package xyz.galaxyy.lualink.lua

import cloud.commandframework.ArgumentDescription
import cloud.commandframework.arguments.CommandArgument
import cloud.commandframework.bukkit.parsers.WorldArgument
import cloud.commandframework.context.CommandContext
import cloud.commandframework.meta.CommandMeta
import com.github.only52607.luakt.CoerceKotlinToLua
import com.github.only52607.luakt.CoerceLuaToKotlin
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.luaj.vm2.*
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.ZeroArgFunction
import xyz.galaxyy.lualink.LuaLink
import xyz.galaxyy.lualink.lua.commands.CommandArgumentMap
import xyz.galaxyy.lualink.lua.commands.LuaCommandHandler
import xyz.galaxyy.lualink.lua.misc.LuaLogger
import java.io.File

// LuaScript contains the Lua script's globals, callbacks, and command and listener handlers and is used to store script state and metadata
class LuaScript(private val plugin: LuaLink, val file: File, val globals: Globals) : LuaTable() {
    internal var onLoadCB: LuaValue? = null
        private set

    internal var onUnloadCB: LuaValue? = null
        private set

    val commands: MutableList<LuaCommandHandler> = mutableListOf()
    val briagadierCommands: MutableList<String> = mutableListOf()
    val listeners: MutableList<Listener> = mutableListOf()
    // Stores task IDs so they can be cancelled on unload
    internal val tasks: MutableList<Int> = mutableListOf()
    init {
        this.set("onLoad", object : VarArgFunction() {
            override fun call(callback: LuaValue): LuaValue {
                if (callback.isfunction()) {
                    onLoadCB = callback
                } else {
                    throw LuaError("onLoad callback must be a function")
                }
                return LuaValue.NIL
            }
        })

        this.set("onUnload", object : VarArgFunction() {
            override fun call(callback: LuaValue): LuaValue {
                if (callback.isfunction()) {
                    onUnloadCB = callback
                } else {
                    throw LuaError("onDisable callback must be a function")
                }
                return LuaValue.NIL
            }
        })

        this.set("registerCommand", object : VarArgFunction() {
            override fun invoke(args: Varargs): Varargs {
                if (args.narg() != 2 || !args.isfunction(1) || !args.istable(2)) {
                    throw IllegalArgumentException("registerSimpleCommand expects 2 arguments: function, string")
                }

                val callback: LuaFunction = args.checkfunction(1)
                val metadata: LuaTable = args.checktable(2)

                if (metadata.get("useCloud").toboolean()) {
                    var builder = this@LuaScript.plugin.paperCommandManager.commandBuilder(metadata.get("name").tojstring())

                    if (!metadata.get("description").isnil())
                        builder = builder.meta(CommandMeta.DESCRIPTION, metadata.get("description").tojstring())
                    builder = if (metadata.get("consoleOnly").toboolean() && !metadata.get("playerOnly").toboolean())
                        builder.senderType(ConsoleCommandSender::class.java)
                    else if (metadata.get("playerOnly").toboolean() && !metadata.get("consoleOnly").toboolean())
                        builder.senderType(org.bukkit.entity.Player::class.java)
                    else
                        builder.senderType(CommandSender::class.java) // Default to CommandSender, can be either console or player, script will need to check if it's a player

                    if (metadata.get("runAsync").toboolean()) {
                        builder = builder.handler { context: CommandContext<CommandSender> ->
                            this@LuaScript.plugin.paperCommandManager.taskRecipe()
                                .begin(context)
                                .asynchronous { c ->
                                    try {
                                        callback.invoke(CoerceKotlinToLua.coerce(c))
                                    } catch (e: LuaError) {
                                        c.sender.sendRichMessage("<red>LuaLink encountered an error while executing this command. ${e.message}")
                                    }
                                }.execute()
                        }
                    } else {
                        builder = builder.handler { context: CommandContext<CommandSender> ->
                            this@LuaScript.plugin.paperCommandManager.taskRecipe()
                                .begin(context)
                                .synchronous { c ->
                                    try {
                                        callback.invoke(CoerceKotlinToLua.coerce(c))
                                    } catch (e: LuaError) {
                                        c.sender.sendRichMessage("<red>LuaLink encountered an error while executing this command. ${e.message}")
                                    }
                                }.execute()
                        }
                    }

                    val commandArgs = metadata.get("args")

                    // Loop through args table and add them to the command
                    for (i in 1..commandArgs.length()) {
                        val arg = commandArgs.get(i)
                        val argType = CommandArgumentMap.Arguments[arg.get("type").tojstring()]
                            ?: throw IllegalArgumentException("Invalid argument type: ${arg.get("type").tojstring()}")

                        // CommandArgument#of is static so no inheritance, so we have to do this. Luckily this only happens once on command registration
                        builder = if (arg.get("optional").toboolean()) {
                            builder.argument(
                                // TODO: Default value support
                                argType.getDeclaredMethod("optional", String::class.java).invoke(null, arg.get("name").tojstring() ?: arg.get("type").tojstring()) as CommandArgument<CommandSender, *>,
                                ArgumentDescription.of(arg.get("description").tojstring() ?: "No description provided"),
                            )
                        } else {
                            builder.argument(
                                argType.getDeclaredMethod("of", String::class.java).invoke(null, arg.get("name").tojstring() ?: arg.get("type").tojstring()) as CommandArgument<CommandSender, *>,
                                ArgumentDescription.of(arg.get("description").tojstring() ?: "No description provided"),
                            )
                        }
                    }
                    builder = builder.permission(metadata.get("permission").tojstring())
                    this@LuaScript.plugin.paperCommandManager.command(builder)
                    this@LuaScript.briagadierCommands.add(metadata.get("name").tojstring())
                    return LuaValue.NIL
                }

                this@LuaScript.registerCommand(callback, metadata)
                return LuaValue.NIL
            }
        })

        this.set("registerSimpleCommand", this.get("registerCommand")) // Compatibility with old API

        this.set("hook", object: VarArgFunction() {
            override fun invoke(args: Varargs): LuaValue {
                val eventName: String = args.checkjstring(1)
                val callback: LuaFunction = args.checkfunction(2)

                if (eventName.isEmpty() || !callback.isfunction()) {
                    throw IllegalArgumentException("hook expects 2 arguments: string, function")
                }

                this@LuaScript.registerListener(eventName, callback)

                return LuaValue.NIL
            }
        })

        this.set("logger", LuaLogger(this.plugin))
        this.set("getServer", object: ZeroArgFunction() {
            override fun call(): LuaValue {
                return CoerceKotlinToLua.coerce(Bukkit.getServer())
            }
        })
    }

    private fun registerCommand(callback: LuaFunction, metadata: LuaTable) {
        val command = LuaCommandHandler(this.plugin, callback, metadata)

        this.commands.add(command)

        this.plugin.server.commandMap.register("lualinkscript", command)
    }
    private fun registerListener(event: String, callback: LuaFunction) {
        try {
            val eventClass = Class.forName(event)
            if (!Event::class.java.isAssignableFrom(eventClass)) {
                throw IllegalArgumentException("Event class must be a subclass of org.bukkit.event.Event")
            }
            val listener = object : Listener {}

            this.plugin.server.pluginManager.registerEvent(eventClass as Class<out org.bukkit.event.Event>, listener, EventPriority.NORMAL, { _, eventObj ->
                if (eventClass.isInstance(eventObj))
                    callback.invoke(CoerceKotlinToLua.coerce(eventObj))
            }, this.plugin)

            this.listeners.add(listener)

        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Event class not found: $event")
        }
    }
}