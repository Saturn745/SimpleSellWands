package xyz.galaxyy.mclua.lua.misc

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import xyz.galaxyy.mclua.MCLua

class LuaLogger : LuaTable() {
    init {
        this.set("info", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                MCLua.getInstance().logger.info(arg.tojstring())
                return LuaValue.NIL
            }
        })

        this.set("warning", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                MCLua.getInstance().logger.warning(arg.tojstring())
                return LuaValue.NIL
            }
        })

        this.set("severe", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                MCLua.getInstance().logger.severe(arg.tojstring())
                return LuaValue.NIL
            }
        })
    }
}