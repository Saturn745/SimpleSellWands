# MCLua Plugin

MCLua is an experimental plugin that provides a basic Lua scripting runtime for Paper-based Minecraft servers. It is designed for small and simple tasks and serves as an alternative to Skript.

## Requirements

To use the MCLua plugin, you need the following:

- A [Paper](https://papermc.io/) based Minecraft server.
- A basic understanding of Lua scripting.

**Note**: Detailed documentation is coming soon.

## **This plugin is very experimental and is not recommended for production use.**

<details>
<summary>Examples</summary>

### Hello command, and toggle flight command
```lua
plugin.onEnable(function()
    plugin.logger.info("Hello world command enabled!!!!!!!! Let's be annoying and make every script spam console when it loads")
end)

plugin.command.register(function(sender, args)
    sender:sendRichMessage("<green>Hello, "..sender:getName())
end, {
    name = "hello",
    description = "Hello world from MCLua"
})

plugin.command.register(function(sender, args)
    if not utils.instanceOf(sender, "org.bukkit.entity.Player") then
        sender:sendRichMessage("<red>This command can only be ran by a player!")
        return
    end
    if not sender:isFlying() then
        sender:setAllowFlight(true)
        sender:setFlying(true)
        sender:sendRichMessage("<green>Enabled flight!")
    else
        sender:setAllowFlight(false)
        sender:setFlying(false)
        sender:sendRichMessage("<red>Disabled flight!")
    end
end, {
    name = "fly",
    description = "Toggle flight"
})
```

### /bring command example
```lua
local function bringCommand(sender, args)
    if args[1] == "*" then
        -- Bring all online players to the command sender's location
        local senderLocation = sender:getLocation()
        local onlinePlayers = plugin.server:getOnlinePlayers()
        for _, player in ipairs(totable(onlinePlayers)) do
            player:teleport(senderLocation)
            player:sendRichMessage("<green>You have been summoned by " .. sender:getName())
        end
        sender:sendRichMessage("<green>You brought all players to your location!")
    else
        -- Bring a specific player to the command sender's location
        local targetPlayer = plugin.server:getPlayer(args[1])
        if targetPlayer then
            local senderLocation = sender:getLocation()
            targetPlayer:teleport(senderLocation)
            targetPlayer:sendRichMessage("<green>You have been summoned by " .. sender:getName())
            sender:sendRichMessage("<green>You brought " .. targetPlayer:getName() .. " to your location!")
        else
            sender:sendRichMessage("<red>Player not found: " .. args[1])
        end
    end
end

function startsWith(str, start)
    return str:sub(1, #start) == start
end



local function bringTabComplete(sender, alias, args)
    if #args == 1 then
        local query = args[1]:lower() -- Convert input query to lowercase for case-insensitive matching
        local onlinePlayers = plugin.server:getOnlinePlayers()
        local suggestions = {}
        
        -- Filter player names based on the input query
        for _, player in ipairs(totable(onlinePlayers)) do
            local playerName = player:getName()
            print(playerName:lower())
            if startsWith(playerName:lower(), query) then
                table.insert(suggestions, playerName)
            end
        end

        -- Add "*" if it matches the query or is empty
        if query == "" or query == "*" then
            table.insert(suggestions, "*")
        end

        return suggestions
    end
    return {}
end

-- Register the /bring command
plugin.command.register(bringCommand, {
    name = "bring",
    description = "Bring a player to your location",
    usage = "/bring <player or *>",
    tabComplete = bringTabComplete
})
```

### Event Handling

```lua
local function listen()
    plugin.event.listen("org.bukkit.event.player.PlayerJoinEvent", function(event)
        local player = event:getPlayer()
        player:playSound(player:getLocation(), "entity.firework_rocket.launch", 1.0, 1.0)
        player:sendRichMessage("<green>Welcome back to the server, " .. player:getName() .. "!")
    end)
end

plugin.onEnable(function()
    plugin.logger.info("Welcomer loaded! PS: You don't have to do this in every script, MCLua will do it for you.")
    listen()
end)
```

</details>
