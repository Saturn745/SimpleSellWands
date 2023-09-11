package xyz.galaxyy.mclua.commands

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import cloud.commandframework.annotations.specifier.Greedy
import org.bukkit.command.CommandSender
import xyz.galaxyy.mclua.MCLua
import xyz.galaxyy.mclua.lua.LuaScript
import java.io.File

class MCLuaCommand {
    @CommandDescription("Reload a Lua script")
    @CommandMethod("mclua reload <script>")
    @CommandPermission("mclua.scripts.reload")
    fun reloadScript(sender: CommandSender, @Argument("script") script: LuaScript) {
        val fileName = script.file.name
        MCLua.getInstance().unLoadScript(script)
        MCLua.getInstance().loadScript(File(MCLua.getInstance().dataFolder, "scripts/$fileName"))
        sender.sendRichMessage("<green>Reloaded script <yellow>$fileName<green>.")
    }

    @CommandDescription("Unload a Lua script")
    @CommandMethod("mclua unload <script>")
    @CommandPermission("mclua.scripts.unload")
    fun unloadScript(sender: CommandSender, @Argument("script") script: LuaScript) {
        MCLua.getInstance().unLoadScript(script)
        sender.sendRichMessage("<green>Unloaded script <yellow>${script.file}<green>.")
    }

    @CommandDescription("Load a Lua script")
    @CommandMethod("mclua load <script>")
    @CommandPermission("mclua.scripts.load")
    fun loadScript(sender: CommandSender, @Argument("script") script: File) {
        MCLua.getInstance().loadScript(script)
        sender.sendRichMessage("<green>Loaded script <yellow>${script.name}<green>.")
    }

    @CommandDescription("Run Lua code")
    @CommandMethod("mclua run <code>")
    @CommandPermission("mclua.scripts.run")
    fun runCode(sender: CommandSender, @Argument("code") @Greedy code: String) {
        // TODO: Run code
    }
}