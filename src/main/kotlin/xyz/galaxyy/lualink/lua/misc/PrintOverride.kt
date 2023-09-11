package xyz.galaxyy.lualink.lua.misc

import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import xyz.galaxyy.lualink.LuaLink

class PrintOverride : OneArgFunction() {
    override fun call(arg: LuaValue): LuaValue? {
        LuaLink.getInstance().logger.info(arg.tojstring())
        return LuaValue.NIL
    }
}