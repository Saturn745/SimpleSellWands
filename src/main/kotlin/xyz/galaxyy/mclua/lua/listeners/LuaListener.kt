package xyz.galaxyy.mclua.lua.listeners

import com.github.only52607.luakt.CoerceKotlinToLua
import org.bukkit.Sound
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction
import xyz.galaxyy.mclua.MCLua
import xyz.galaxyy.mclua.lua.wrappers.LuaPluginWrapper

class LuaListener(val pluginWrapper: LuaPluginWrapper) : LuaTable() {
    init {
        this.set("listen", object: VarArgFunction() {
            override fun invoke(args: Varargs): LuaValue {
                val eventName: String = args.checkjstring(1)
                val callback: LuaFunction = args.checkfunction(2)

                if (eventName.isEmpty() || !callback.isfunction()) {
                    throw IllegalArgumentException("register expects 2 arguments: string, function")
                }

                pluginWrapper.registerListener(eventName, callback)

                return LuaValue.NIL
            }
        })
    }
}