package xyz.galaxyy.lualink

import cloud.commandframework.annotations.*
import cloud.commandframework.arguments.parser.ParserParameters
import cloud.commandframework.arguments.parser.StandardParameters
import cloud.commandframework.bukkit.CloudBukkitCapabilities
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.execution.FilteringCommandSuggestionProcessor
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.paper.PaperCommandManager
import com.github.only52607.luakt.CoerceKotlinToLua
import com.github.only52607.luakt.lib.LuaKotlinExLib
import com.github.only52607.luakt.lib.LuaKotlinLib
import io.leangen.geantyref.TypeToken
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform
import xyz.galaxyy.lualink.commands.AvailableScriptParser
import xyz.galaxyy.lualink.commands.LoadedScriptParser
import xyz.galaxyy.lualink.commands.MCLuaCommand
import java.util.function.Function
import xyz.galaxyy.lualink.lua.LuaScript
import xyz.galaxyy.lualink.lua.LuaUtils
import xyz.galaxyy.lualink.lua.misc.PrintOverride
import xyz.galaxyy.lualink.lua.wrappers.LuaEnumWrapper
import xyz.galaxyy.lualink.lua.wrappers.LuaPluginWrapper
import java.io.File

class LuaLink : JavaPlugin() {
    val loadedScripts: MutableList<LuaScript> = mutableListOf()
    lateinit var manager: PaperCommandManager<CommandSender>
    lateinit var annotationParser: AnnotationParser<CommandSender>
    companion object {
        private lateinit var instance: LuaLink
        fun getInstance(): LuaLink {
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
        this.setupCloud()
        this.registerCommands()
        this.loadedScripts.forEach { script ->
            if (script.pluginWrapper.onEnableCB?.isfunction() == true) {
                script.pluginWrapper.onEnableCB?.call()
            }
        }
    }

    override fun onDisable() {
        val scriptsToUnload = loadedScripts.toList() // Make a copy of the list

        scriptsToUnload.forEach { script ->
            unLoadScript(script)
        }
    }
    private fun registerCommands() {
        this.annotationParser.parse(MCLuaCommand())
    }
    private fun setupCloud() {

            val executionCoordinatorFunction = CommandExecutionCoordinator.simpleCoordinator<CommandSender>()

            val mapperFunction: Function<CommandSender, CommandSender> = Function.identity()
            try {
                this.manager = PaperCommandManager( /* Owning plugin */
                    this,  /* Coordinator function */
                    executionCoordinatorFunction,  /* Command Sender -> C */
                    mapperFunction,  /* C -> Command Sender */
                    mapperFunction
                )
            } catch (e: Exception) {
                getLogger().severe("Failed to initialize the command this.manager")
                /* Disable the plugin */server.pluginManager.disablePlugin(this)
                return
            }

            // Use contains to filter suggestions instead of default startsWith

            // Use contains to filter suggestions instead of default startsWith
            manager.commandSuggestionProcessor(
                FilteringCommandSuggestionProcessor(
                    FilteringCommandSuggestionProcessor.Filter.contains<CommandSender>(true).andTrimBeforeLastSpace()
                )
            )

            if (this.manager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
                this.manager.registerBrigadier()
            }

            if (this.manager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                this.manager.registerAsynchronousCompletions()
            }



            val commandMetaFunction: Function<ParserParameters, CommandMeta> =
                Function<ParserParameters, CommandMeta> { p ->
                    CommandMeta.simple() // This will allow you to decorate commands with descriptions
                        .with(CommandMeta.DESCRIPTION, p.get(StandardParameters.DESCRIPTION, "No description"))
                        .build()
                }
            this.annotationParser = AnnotationParser( /* Manager */
                this.manager,  /* Command sender type */
                CommandSender::class.java,  /* Mapper for command meta instances */
                commandMetaFunction
            )

        this.manager.parserRegistry().registerParserSupplier(
            TypeToken.get(LuaScript::class.java)
        ) { LoadedScriptParser() }

        this.manager.parserRegistry().registerParserSupplier(
            TypeToken.get(File::class.java)
        ) { AvailableScriptParser() }

    }


    // Should probably move this to a ScriptManager class
    fun loadScript(file: File) {
        val globals = JsePlatform.standardGlobals()
        val pluginWrapper = LuaPluginWrapper()
        val script = LuaScript(file, globals, pluginWrapper)
        globals.load(LuaKotlinLib())
        globals.load(LuaKotlinExLib())
        globals.set("plugin", CoerceKotlinToLua.coerce(pluginWrapper))
        globals.set("print", CoerceJavaToLua.coerce(PrintOverride()))
        globals.set("utils", CoerceKotlinToLua.coerce(LuaUtils()))
        globals.set("enums", CoerceKotlinToLua.coerce(LuaEnumWrapper()))
        this.logger.info("Loading script ${file.name}")
        globals.loadfile(file.path).call()
        loadedScripts.add(script)
        Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
        if (this.isEnabled)
            script.pluginWrapper.onEnableCB?.call()
        this.logger.info("Loaded script ${file.name}")
    }

    fun unLoadScript(script: LuaScript) {
        script.pluginWrapper.listeners.forEach { listener ->
            HandlerList.unregisterAll(listener)
        }
        script.pluginWrapper.commands.forEach { command ->
            command.unregister(this.server.commandMap)
            this.server.commandMap.knownCommands.remove(command.name)
            command.aliases.forEach { alias ->
                this.server.commandMap.knownCommands.remove(alias)
            }
            Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
        }
        if (script.pluginWrapper.onDisableCB?.isfunction() == true) {
            script.pluginWrapper.onDisableCB?.call()
        }
        this.loadedScripts.remove(script)
    }

    private fun loadScripts() {
        this.logger.info("Loading scripts...")
        if (!File(this.dataFolder.path+"/scripts").exists()) {
            File(this.dataFolder.path+"/scripts").mkdirs()
        }

        File(this.dataFolder.path+"/scripts").walk().forEach { file ->
            if (file.extension == "lua") {
                if (file.name.startsWith(".")) {
                    return@forEach
                }
                this.loadScript(file)
            } else {
                if (file.name != "scripts") {
                    this.logger.warning("${file.name} is in the scripts folder but is not a lua file!")
                }
            }
        }
    }
}
