package xyz.galaxyy.mclua.lua.wrappers

import com.github.only52607.luakt.CoerceKotlinToLua
import org.bukkit.Bukkit
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import xyz.galaxyy.mclua.MCLua
import xyz.galaxyy.mclua.lua.commands.LuaCommand
import xyz.galaxyy.mclua.lua.listeners.LuaListener
import xyz.galaxyy.mclua.lua.misc.LuaLogger


class LuaPluginWrapper : LuaTable() {
    public var onLoadCB: LuaValue? = null
    public var onEnableCB: LuaValue? = null
    public var onDisableCB: LuaValue? = null

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
        this.set("command", CoerceKotlinToLua.coerce(LuaCommand()))
        this.set("event", CoerceKotlinToLua.coerce(LuaListener()))
    }
}