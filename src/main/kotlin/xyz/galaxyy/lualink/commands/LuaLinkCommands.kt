package xyz.galaxyy.lualink.commands

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import cloud.commandframework.annotations.specifier.Greedy
import org.bukkit.command.CommandSender
import xyz.galaxyy.lualink.LuaLink
import xyz.galaxyy.lualink.lua.LuaScript
import java.io.File

class LuaLinkCommands(private val plugin: LuaLink) {
    @CommandDescription("Reload a Lua script")
    @CommandMethod("lualink reload <script>")
    @CommandPermission("lualink.scripts.reload")
    fun reloadScript(sender: CommandSender, @Argument("script") script: LuaScript) {
        val fileName = script.file.name
        this.plugin.unLoadScript(script)
        this.plugin.loadScript(File(this.plugin.dataFolder, "scripts/$fileName"))
        sender.sendRichMessage("<green>Reloaded script <yellow>$fileName<green>.")
    }

    @CommandDescription("Unload a Lua script")
    @CommandMethod("lualink unload <script>")
    @CommandPermission("lualink.scripts.unload")
    fun unloadScript(sender: CommandSender, @Argument("script") script: LuaScript) {
        this.plugin.unLoadScript(script)
        sender.sendRichMessage("<green>Unloaded script <yellow>${script.file}<green>.")
    }

    @CommandDescription("Load a Lua script")
    @CommandMethod("lualink load <script>")
    @CommandPermission("lualink.scripts.load")
    fun loadScript(sender: CommandSender, @Argument("script") script: File) {
        this.plugin.loadScript(script)
        sender.sendRichMessage("<green>Loaded script <yellow>${script.name}<green>.")
    }

    @CommandDescription("Run Lua code")
    @CommandMethod("lualink run <code>")
    @CommandPermission("lualink.scripts.run")
    fun runCode(sender: CommandSender, @Argument("code") @Greedy code: String) {
        // TODO: Run code
    }
}