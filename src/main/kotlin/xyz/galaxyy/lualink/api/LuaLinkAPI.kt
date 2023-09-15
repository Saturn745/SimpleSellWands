package xyz.galaxyy.lualink.api

import org.luaj.vm2.LuaTable
import xyz.galaxyy.lualink.api.addons.LuaAddon

class LuaLinkAPI {
    companion object {
        private val registeredAddons = mutableMapOf<String, LuaAddon>()
        private val addonCache = mutableMapOf<String, LuaTable>()

        fun registerAddon(name: String, addon: LuaAddon) {
            registeredAddons[name] = addon
            // Cache the addon's LuaTable when registering it.
            val addonTable = LuaTable()
            for (func in addon.getFunctions()) {
                addonTable.set(func.key, func.value)
            }
            for (table in addon.getTables()) {
                addonTable.set(table.key, table.value)
            }
            addonCache[name] = addonTable
        }

        fun unregisterAddon(name: String) {
            registeredAddons.remove(name)
            addonCache.remove(name)
        }

        internal fun getAddon(name: String): LuaAddon? {
            return registeredAddons[name]
        }

        internal fun getAddonFromCache(name: String): LuaTable? {
            return addonCache[name]
        }
    }
}