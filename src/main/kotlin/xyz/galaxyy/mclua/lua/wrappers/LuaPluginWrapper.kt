package xyz.galaxyy.mclua.lua.wrappers

import com.github.only52607.luakt.CoerceKotlinToLua
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import xyz.galaxyy.mclua.MCLua
import xyz.galaxyy.mclua.lua.commands.LuaCommand
import xyz.galaxyy.mclua.lua.commands.LuaCommandHandler
import xyz.galaxyy.mclua.lua.listeners.LuaListener
import xyz.galaxyy.mclua.lua.misc.LuaLogger


class LuaPluginWrapper : LuaTable() {
    var onLoadCB: LuaValue? = null
        private set

    var onEnableCB: LuaValue? = null
        private set

    var onDisableCB: LuaValue? = null
        private set

    val commands: MutableList<LuaCommandHandler> = mutableListOf()
    val listeners: MutableList<Listener> = mutableListOf()

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

        this.set("onEnable", object : VarArgFunction() {
            override fun call(callback: LuaValue): LuaValue {
                if (callback.isfunction()) {
                    onEnableCB = callback
                } else {
                    throw LuaError("onEnable callback must be a function")
                }
                return LuaValue.NIL
            }
        })

        this.set("onDisable", object : VarArgFunction() {
            override fun call(callback: LuaValue): LuaValue {
                if (callback.isfunction()) {
                    onDisableCB = callback
                } else {
                    throw LuaError("onDisable callback must be a function")
                }
                return LuaValue.NIL
            }
        })

        this.set("logger", CoerceKotlinToLua.coerce(LuaLogger()))
        this.set("server", CoerceKotlinToLua.coerce(Bukkit.getServer()))
        this.set("command", CoerceKotlinToLua.coerce(LuaCommand(this)))
        this.set("event", CoerceKotlinToLua.coerce(LuaListener(this)))
    }

    fun registerCommand(callback: LuaFunction, metadata: LuaTable) {
        val command: LuaCommandHandler = LuaCommandHandler(callback, metadata)

        this.commands.add(command)

        MCLua.getInstance().server.commandMap.register("mclua", command)
    }
    fun registerListener(event: String, callback: LuaFunction) {
        try {
            val eventClass = Class.forName(event)
            if (!Event::class.java.isAssignableFrom(eventClass)) {
                throw IllegalArgumentException("Event class must be a subclass of org.bukkit.event.Event")
            }
            val listener = object : Listener {}

            MCLua.getInstance().server.pluginManager.registerEvent(eventClass as Class<out org.bukkit.event.Event>, listener, EventPriority.NORMAL, { _, event ->
                callback.invoke(CoerceKotlinToLua.coerce(event))
            }, MCLua.getInstance())

            this.listeners.add(listener)

        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Event class not found: $event")
        }
    }
}