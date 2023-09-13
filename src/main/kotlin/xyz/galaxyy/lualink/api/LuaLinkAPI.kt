package xyz.galaxyy.lualink.api

import xyz.galaxyy.lualink.api.addons.LuaAddon

class LuaLinkAPI {
    companion object {
        private val registeredAddons = mutableMapOf<String, LuaAddon>()

        fun registerAddon(name: String, addon: LuaAddon) {
            registeredAddons[name] = addon
        }

        internal fun getAddon(name: String): LuaAddon? {
            return registeredAddons[name]
        }
    }
}