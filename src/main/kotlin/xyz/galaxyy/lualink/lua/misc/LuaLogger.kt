package xyz.galaxyy.lualink.lua.misc

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import xyz.galaxyy.lualink.LuaLink

class LuaLogger(private val plugin: LuaLink) : LuaTable() {
    init {
        this.set("info", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                plugin.logger.info(arg.tojstring())
                return LuaValue.NIL
            }
        })

        this.set("warning", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                plugin.logger.warning(arg.tojstring())
                return LuaValue.NIL
            }
        })

        this.set("severe", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                plugin.logger.severe(arg.tojstring())
                return LuaValue.NIL
            }
        })
    }
}