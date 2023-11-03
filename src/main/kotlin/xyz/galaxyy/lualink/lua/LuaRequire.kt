package xyz.galaxyy.lualink.lua

import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import xyz.galaxyy.lualink.LuaLink

class LuaRequire(private val plugin: LuaLink, private val scriptManager: LuaScriptManager) : OneArgFunction() {
    override fun call(arg: LuaValue): LuaValue {
        val scriptFileName = arg.checkjstring() + ".lua"
        val scriptFile = this.plugin.dataFolder.resolve("lua-libraries").resolve(scriptFileName)
        if (!scriptFile.exists()) {
            return LuaValue.error("Script $scriptFileName does not exist.")
        }
        return scriptManager.loadScript(scriptFile) ?: return error("Failed to load script $scriptFileName.")
    }
}