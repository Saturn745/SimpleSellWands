package xyz.galaxyy.mclua.lua.misc

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import xyz.galaxyy.mclua.MCLua

class PrintOverride : OneArgFunction() {
    override fun call(arg: LuaValue): LuaValue? {
        MCLua.getInstance().logger.info(arg.tojstring())
        return LuaValue.NIL
    }
}