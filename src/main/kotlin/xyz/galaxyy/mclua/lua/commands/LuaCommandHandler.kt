package xyz.galaxyy.mclua.lua.commands

import com.github.only52607.luakt.CoerceKotlinToLua
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import xyz.galaxyy.mclua.MCLua


class LuaCommandHandler(private val callback: LuaFunction, private val metadata: LuaTable) : Command(metadata.get("name").tojstring()) {
    init {
        if (!this.metadata.get("description").isnil())
            this.description = this.metadata.get("description").tojstring()
        if (!this.metadata.get("usage").isnil())
            this.usage = this.metadata.get("usage").tojstring()
        if (!this.metadata.get("permission").isnil())
            this.permission = this.metadata.get("permission").tojstring()
        if (this.metadata.get("aliases").isnil()) {
            this.aliases = mutableListOf()
        } else {
            val aliases: MutableList<String> = ArrayList()
            for (i in 1..this.metadata.get("aliases").length()) {
                aliases.add(this.metadata.get("aliases").get(i).tojstring())
            }
            this.aliases = aliases
        }
    }
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        if (this.metadata.get("consoleOnly").toboolean() && sender !is Player) {
            sender.sendRichMessage("<red>This command can only be executed by players.")
            return true
        }

        if (this.metadata.get("runAsync").toboolean()) {
            Bukkit.getScheduler().runTaskAsynchronously(MCLua.getInstance(), Runnable {
                callback.invoke(CoerceKotlinToLua.coerce(sender), CoerceKotlinToLua.coerce(args))
            })
        } else {
            callback.invoke(CoerceKotlinToLua.coerce(sender), CoerceKotlinToLua.coerce(args))
        }
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>?): MutableList<String> {
        val tabCompleteFunction = this.metadata.get("tabComplete")

        if (tabCompleteFunction.isfunction()) {
            val luaSender = CoerceKotlinToLua.coerce(sender)
            val luaAlias = LuaValue.valueOf(alias)
            val luaArgs = CoerceKotlinToLua.coerce(args)

            val result = tabCompleteFunction.call(luaSender, luaAlias, luaArgs)

            if (result.istable()) {
                val suggestions = mutableListOf<String>()
                for (i in 1..result.length()) {
                    suggestions.add(result.get(i).tojstring())
                }
                return suggestions
            }
        }

        // Default behavior: Return online player names
        return Bukkit.getOnlinePlayers().map { player -> player.name }.toMutableList()
    }



}