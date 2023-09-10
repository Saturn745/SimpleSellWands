package xyz.galaxyy.mclua

import com.github.only52607.luakt.CoerceKotlinToLua
import com.github.only52607.luakt.lib.LuaKotlinExLib
import com.github.only52607.luakt.lib.LuaKotlinLib
import org.bukkit.plugin.java.JavaPlugin
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform
import xyz.galaxyy.mclua.lua.LuaScript
import xyz.galaxyy.mclua.lua.LuaUtils
import xyz.galaxyy.mclua.lua.misc.PrintOverride
import xyz.galaxyy.mclua.lua.wrappers.LuaEnumWrapper
import xyz.galaxyy.mclua.lua.wrappers.LuaPluginWrapper
import java.io.File

class MCLua : JavaPlugin() {
    private val loadedScripts: MutableList<LuaScript> = mutableListOf()
    companion object {
        private lateinit var instance: MCLua
        fun getInstance(): MCLua {
            return instance
        }
    }

    override fun onLoad() {
        instance = this
        this.loadScripts()
        this.loadedScripts.forEach { script ->
            if (script.pluginWrapper.onLoadCB?.isfunction() == true) {
                script.pluginWrapper.onLoadCB?.call()
            }
        }
    }

    override fun onEnable() {
        this.loadedScripts.forEach { script ->
            if (script.pluginWrapper.onEnableCB?.isfunction() == true) {
                script.pluginWrapper.onEnableCB?.call()
            }
        }
    }

    override fun onDisable() {
        this.loadedScripts.forEach { script ->
            if (script.pluginWrapper.onDisableCB?.isfunction() == true) {
                script.pluginWrapper.onDisableCB?.call()
            }
        }
    }

    private fun loadScripts() {
        this.logger.info("Loading scripts...")
        if (!File(this.dataFolder.path+"/scripts").exists()) {
            File(this.dataFolder.path+"/scripts").mkdirs()
        }

        File(this.dataFolder.path+"/scripts").walk().forEach { file ->
            if (file.extension == "lua") {
                val globals = JsePlatform.standardGlobals()
                val pluginWrapper = LuaPluginWrapper()
                val script = LuaScript(file.name, globals, pluginWrapper)
                globals.load(LuaKotlinLib())
                globals.load(LuaKotlinExLib())
                globals.set("plugin", CoerceKotlinToLua.coerce(pluginWrapper))
                globals.set("print", CoerceJavaToLua.coerce(PrintOverride()))
                globals.set("utils", CoerceKotlinToLua.coerce(LuaUtils()))
                globals.set("enums", CoerceKotlinToLua.coerce(LuaEnumWrapper()))
                this.logger.info("Loading script ${file.name}")
                globals.loadfile(file.path).call()
                loadedScripts.add(script)
                this.logger.info("Loaded script ${file.name}")
            } else {
                if (file.name != "scripts") {
                    this.logger.warning("${file.name} is in the scripts folder but is not a lua file!")
                }
            }
        }
    }
}
