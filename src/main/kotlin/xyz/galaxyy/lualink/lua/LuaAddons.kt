package xyz.galaxyy.lualink.lua

import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.OneArgFunction
import xyz.galaxyy.lualink.api.LuaLinkAPI

internal class LuaAddons : LuaTable() {
    init {
        this.set("get", object : OneArgFunction() {
            override fun call(arg: org.luaj.vm2.LuaValue?): org.luaj.vm2.LuaValue {
                if (arg == null) {
                    throw IllegalArgumentException("get expects 1 argument: addonName")
                }
                val addonName = arg.checkjstring()
                val addon = LuaLinkAPI.getAddon(addonName) ?: throw IllegalArgumentException("Addon not found")

                val addonTable = LuaTable()

                for (func in addon.getFunctions()) {
                    addonTable.set(func.key, func.value)
                }

                for (table in addon.getTables()) {
                    addonTable.set(table.key, table.value)
                }

                return addonTable
            }
        })

    }
}