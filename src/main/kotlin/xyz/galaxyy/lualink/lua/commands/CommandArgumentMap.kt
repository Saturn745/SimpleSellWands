package xyz.galaxyy.lualink.lua.commands

import cloud.commandframework.arguments.standard.*
import cloud.commandframework.bukkit.parsers.*
import cloud.commandframework.bukkit.parsers.selector.MultiplePlayerSelectorArgument
import cloud.commandframework.bukkit.parsers.selector.SingleEntitySelectorArgument
import cloud.commandframework.bukkit.parsers.selector.SinglePlayerSelectorArgument
import cloud.commandframework.paper.argument.KeyedWorldArgument

class CommandArgumentMap {
    companion object {
        // Any other way to do this?
        val Arguments = mapOf(
            // Standard arguments
            "boolean" to BooleanArgument::class.java,
            "byte" to ByteArgument::class.java,
            "char" to CharArgument::class.java,
            "double" to DoubleArgument::class.java,
            "duration" to DurationArgument::class.java,
            "enum" to EnumArgument::class.java,
            "float" to FloatArgument::class.java,
            "integer" to IntegerArgument::class.java,
            "long" to LongArgument::class.java,
            "short" to ShortArgument::class.java,
            "string" to StringArgument::class.java,
            "stringArray" to StringArrayArgument::class.java,
            "uuid" to UUIDArgument::class.java,

            // Paper arguments
            "keyedWorld" to KeyedWorldArgument::class.java,

            // Bukkit arguments
            "blockPredicate" to BlockPredicateArgument::class.java,
            "enchantment" to EnchantmentArgument::class.java,
            "itemStack" to ItemStackArgument::class.java,
            "itemStackPredicate" to ItemStackPredicateArgument::class.java,
            "material" to MaterialArgument::class.java,
            "offlinePlayer" to OfflinePlayerArgument::class.java,
            "player" to PlayerArgument::class.java,
            "world" to WorldArgument::class.java,
            "singlePlayerSelector" to SinglePlayerSelectorArgument::class.java,
            "singleEntitySelector" to SingleEntitySelectorArgument::class.java,
            "multiPlayerSelector" to MultiplePlayerSelectorArgument::class.java,
            "multiEntitySelector" to MultiplePlayerSelectorArgument::class.java,
        )
    }
}