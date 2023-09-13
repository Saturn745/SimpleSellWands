local function bringCommand(sender, args)
    if #args == 0 then
        -- No arguments provided, send usage information
        sender:sendRichMessage("<red>Usage: /bring <player or *>")
        return
    end

    if args[1] == "*" then
        -- Bring all online players to the command sender's location
        local senderLocation = sender:getLocation()
        local onlinePlayers = script.getServer():getOnlinePlayers()
        for _, player in ipairs(totable(onlinePlayers)) do
            player:teleport(senderLocation)
            player:sendRichMessage("<green>You have been summoned by " .. sender:getName())
        end
        sender:sendRichMessage("<green>You brought all players to your location!")
    else
        -- Bring a specific player to the command sender's location
        local targetPlayer = script.getServer():getPlayer(args[1])
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

local function startsWith(str, start)
    return str:sub(1, #start) == start
end

local function bringTabComplete(sender, alias, args)
    if #args == 1 then
        local query = args[1]:lower() -- Convert input query to lowercase for case-insensitive matching
        local onlinePlayers = script.getServer():getOnlinePlayers()
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
script.registerSimpleCommand(bringCommand, {
    name = "bring",
    description = "Bring a player to your location",
    usage = "/bring <player or *>",
    tabComplete = bringTabComplete
})
