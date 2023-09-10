package xyz.galaxyy.mclua.lua.wrappers

import org.luaj.vm2.LuaTable
import xyz.galaxyy.mclua.lua.enums.LuaSoundEnum

class EnumWrapper : LuaTable() {
    init {
        this.set("Sound", LuaSoundEnum())
    }
}