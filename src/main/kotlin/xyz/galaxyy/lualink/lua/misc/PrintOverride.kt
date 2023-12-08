package xyz.galaxyy.lualink.lua.misc

import net.kyori.adventure.text.minimessage.MiniMessage
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import xyz.galaxyy.lualink.LuaLink

class PrintOverride(private val plugin: LuaLink) : OneArgFunction() {
    override fun call(arg: LuaValue): LuaValue? {
        this.plugin.componentLogger.info(MiniMessage.miniMessage().deserialize(arg.tojstring()))
        return LuaValue.NIL
    }
}