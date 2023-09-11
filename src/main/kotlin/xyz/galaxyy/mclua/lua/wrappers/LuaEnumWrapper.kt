package xyz.galaxyy.mclua.lua.wrappers

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.persistence.PersistentDataType
import org.luaj.vm2.LuaTable

class LuaEnumWrapper : LuaTable() {
    init {
        this.set("Sound", LuaEnum(Sound::class))
        this.set("Material", LuaEnum(Material::class))
        this.set("EntityType", LuaEnum(EntityType::class))
    }
}