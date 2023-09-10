package xyz.galaxyy.mclua.lua.enums

import com.github.only52607.luakt.CoerceKotlinToLua
import org.bukkit.Sound
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.OneArgFunction

class LuaSoundEnum : LuaTable() {
    init {
        this.set("valueOf", object: OneArgFunction() {
            override fun call(arg: org.luaj.vm2.LuaValue?): org.luaj.vm2.LuaValue {
                if (arg == null) {
                    throw IllegalArgumentException("valueOf expects 1 argument: string")
                }

                val soundName = arg.checkjstring()

                return CoerceKotlinToLua.coerce(org.bukkit.Sound.valueOf(soundName))
            }
        })

        this.set("values", object: OneArgFunction() {
            override fun call(arg: org.luaj.vm2.LuaValue?): org.luaj.vm2.LuaValue {
                if (arg == null) {
                    throw IllegalArgumentException("values expects 1 argument: string")
                }

                val soundName = arg.checkjstring()

                return CoerceKotlinToLua.coerce(Sound.entries.toTypedArray())
            }
        })

        for (sound in Sound.entries) {
            this.set(sound.name, CoerceKotlinToLua.coerce(sound))
        }
    }
}