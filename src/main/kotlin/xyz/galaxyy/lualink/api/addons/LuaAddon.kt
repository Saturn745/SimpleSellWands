package xyz.galaxyy.lualink.api.addons

import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable

class LuaAddon() {
    private val registeredFunctions = mutableMapOf<String, LuaFunction>()
    private val registeredTables = mutableMapOf<String, LuaTable>()

    fun addFunction(name: String, function: LuaFunction) {
        this.registeredFunctions[name] = function
    }

    fun getFunctions(): MutableMap<String, LuaFunction> {
        return this.registeredFunctions
    }

    fun addTable(name: String, table: LuaTable) {
        this.registeredTables[name] = table
    }

    fun getTables(): MutableMap<String, LuaTable> {
        return this.registeredTables
    }
}