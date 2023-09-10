package xyz.galaxyy.mclua.lua.commands

import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.VarArgFunction
import xyz.galaxyy.mclua.MCLua

class LuaCommand : LuaTable() {
    init {
        this.set("register", object : VarArgFunction() {
            override fun invoke(args: Varargs): Varargs {
                if (args.narg() != 2 || !args.isfunction(1) || !args.istable(2)) {
                    throw IllegalArgumentException("register expects 2 arguments: function, string")
                }

                val callback: LuaFunction = args.checkfunction(1)
                val metadata: LuaTable = args.checktable(2)

                val command: LuaCommandHandler = LuaCommandHandler(callback, metadata)

                MCLua.getInstance().server.commandMap.register("mclua", command)

                return LuaValue.NIL
            }
        })
        this.set("isSenderPlayer", object: OneArgFunction() {
            override fun call(arg: LuaValue?): LuaValue {
                if (arg == null) {
                    throw IllegalArgumentException("isSenderPlayer expects 1 argument: sender")
                }

                if (arg.isuserdata()) {
                    return LuaValue.valueOf(arg.checkuserdata().javaClass.simpleName == "CraftPlayer")
                }

                return LuaValue.FALSE
            }
        })
    }
}