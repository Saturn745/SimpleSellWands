package xyz.galaxyy.lualink

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.arguments.parser.ParserParameters
import cloud.commandframework.arguments.parser.StandardParameters
import cloud.commandframework.bukkit.CloudBukkitCapabilities
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.execution.FilteringCommandSuggestionProcessor
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.paper.PaperCommandManager
import com.github.only52607.luakt.lib.LuaKotlinExLib
import com.github.only52607.luakt.lib.LuaKotlinLib
import io.leangen.geantyref.TypeToken
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import org.luaj.vm2.LuaError
import org.luaj.vm2.lib.jse.JsePlatform
import xyz.galaxyy.lualink.commands.AvailableScriptParser
import xyz.galaxyy.lualink.commands.LoadedScriptParser
import xyz.galaxyy.lualink.commands.LuaLinkCommands
import xyz.galaxyy.lualink.lua.LuaImport
import xyz.galaxyy.lualink.lua.LuaUtils
import xyz.galaxyy.lualink.lua.misc.PrintOverride
import xyz.galaxyy.lualink.lua.wrappers.LuaEnumWrapper
import xyz.galaxyy.lualink.lua.wrappers.LuaScript
import java.io.File
import java.util.function.Function

class LuaLink : JavaPlugin() {
    val loadedScripts: MutableList<LuaScript> = mutableListOf()
    lateinit var manager: PaperCommandManager<CommandSender>
    lateinit var annotationParser: AnnotationParser<CommandSender>

    override fun onLoad() {
        this.loadScripts()
    }

    override fun onEnable() {
        this.setupCloud()
        this.registerCommands()
        this.loadedScripts.forEach { script ->
            if (script.onEnableCB?.isfunction() == true) {
                try {
                    script.onEnableCB?.call()
                } catch (e: LuaError) {
                    this.logger.severe("LuaLink encountered an error while called onEnable for ${script.file.name}: ${e.message}")
                    e.printStackTrace()
                    return
                }
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
        this.annotationParser.parse(LuaLinkCommands(this))
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
        ) { LoadedScriptParser(this) }

        this.manager.parserRegistry().registerParserSupplier(
            TypeToken.get(File::class.java)
        ) { AvailableScriptParser(this) }

    }


    // Should probably move this to a ScriptManager class
    fun loadScript(file: File) {
        val globals = JsePlatform.standardGlobals()
        val script = LuaScript(this, file, globals)
        globals.load(LuaKotlinLib())
        globals.load(LuaKotlinExLib())
        globals.set("script", script)
        globals.set("print", PrintOverride(this))
        globals.set("utils", LuaUtils(this))
        globals.set("enums", LuaEnumWrapper())
        globals.set("import", LuaImport())
        this.logger.info("Loading script ${file.name}")
        try {
            globals.loadfile(file.path).call()
        } catch (e: LuaError) {
            this.logger.severe("LuaLink encountered an error while loading ${file.name}: ${e.message}")
            return
        }
        loadedScripts.add(script)
        if (script.onLoadCB?.isfunction() == true) {
            try {
                script.onLoadCB?.call()
            } catch (e: LuaError) {
                this.logger.severe("LuaLink encountered an error while called onLoad for ${file.name}: ${e.message}")
                return
            }
        }
        Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
        if (this.isEnabled) {
            try {
                script.onEnableCB?.call()
            } catch (e: LuaError) {
                this.logger.severe("LuaLink encountered an error while called onEnable for ${file.name}: ${e.message}")
                return
            }
        }
        this.logger.info("Loaded script ${file.name}")
    }

    fun unLoadScript(script: LuaScript) {
        script.listeners.forEach { listener ->
            HandlerList.unregisterAll(listener)
        }
        script.commands.forEach { command ->
            command.unregister(this.server.commandMap)
            this.server.commandMap.knownCommands.remove(command.name)
            command.aliases.forEach { alias ->
                this.server.commandMap.knownCommands.remove(alias)
            }
            Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
        }
        if (script.onDisableCB?.isfunction() == true) {
            try {
                script.onDisableCB?.call()
            } catch (e: LuaError) {
                this.logger.severe("LuaLink encountered an error while called onDisable for ${script.file.name}: ${e.message}")
                return
            }
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
