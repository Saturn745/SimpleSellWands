local function listen()
    plugin.hook("org.bukkit.event.player.PlayerJoinEvent", function(event)
        local player = event:getPlayer()
        player:playSound(player:getLocation() ,enums.Sound.ENTITY_FIREWORK_ROCKET_BLAST, 10.0, 10.0)

        player:sendRichMessage("<green>Welcome back to the server, " .. player:getName() .. "! This has to much voodoo")
    end)
end

plugin.onEnable(function()
    plugin.logger.info("Welcomer loaded! PS: You don't have to do this in every script, MCServer will do it for you.")
    listen()
end)
