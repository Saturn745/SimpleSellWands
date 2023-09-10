package xyz.galaxyy.mclua.lua.listeners

import com.github.only52607.luakt.CoerceKotlinToLua
import org.bukkit.Sound
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction
import xyz.galaxyy.mclua.MCLua

class LuaListener : LuaTable() {
    init {
        this.set("listen", object: VarArgFunction() {
            override fun invoke(args: Varargs): LuaValue {
                val eventName: String = args.checkjstring(1)
                val callback: LuaValue = args.checkfunction(2)

                if (eventName.isEmpty() || !callback.isfunction()) {
                    throw IllegalArgumentException("register expects 2 arguments: string, function")
                }

                try {
                    val eventClass = Class.forName(eventName)
                    if (!Event::class.java.isAssignableFrom(eventClass)) {
                        throw IllegalArgumentException("Event class must be a subclass of org.bukkit.event.Event")
                    }
                    MCLua.getInstance().server.pluginManager.registerEvent(eventClass as Class<out org.bukkit.event.Event>, object: Listener {}, EventPriority.NORMAL, { _, event ->
                        callback.invoke(CoerceKotlinToLua.coerce(event))
                    }, MCLua.getInstance())
                } catch (e: ClassNotFoundException) {
                    throw IllegalArgumentException("Event class not found: $eventName")
                }

                return LuaValue.NIL
            }
        })
    }
}