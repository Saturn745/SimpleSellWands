local lastMessaged = {} -- Store the last person messaged for /r (using UUIDs)

-- Function to send a private message
local function sendPrivateMessage(sender, target, message)
    -- Check if the target player is online
    local targetPlayer = script.getServer():getPlayer(target)
    if targetPlayer then
        -- Send the message to the target
        targetPlayer:sendRichMessage("<gray>[From " .. sender:getName() .. "]: <reset>" .. message)
        sender:sendRichMessage("<gray>[To " .. targetPlayer:getName() .. "]: <reset>" .. message)

        -- Update last messaged players for both sender and target (using UUIDs)
        lastMessaged[sender:getUniqueId()] = targetPlayer:getUniqueId()
        lastMessaged[targetPlayer:getUniqueId()] = sender:getUniqueId()
    else
        sender:sendRichMessage("<red>Player not found or is not online: " .. tostring(target))
    end
end

-- Register the /msg command
script.registerSimpleCommand(function(sender, args)
    if #args < 2 then
        sender:sendRichMessage("<red>Usage: /msg <player> <message>")
        return
    end

    local target = args[1]
    table.remove(args, 1) -- Remove the target from the args
    local message = table.concat(args, " ")

    sendPrivateMessage(sender, target, message)
end, {
    name = "msg",
    description = "Send a private message to a player",
    usage = "/msg <player> <message>"
})

-- Register the /r command
script.registerSimpleCommand(function(sender, args)
    local lastTargetUUID = lastMessaged[sender:getUniqueId()]

    if not lastTargetUUID then
        sender:sendRichMessage("<red>No player to reply to.")
        return
    end

    local lastTarget = script.getServer():getPlayer(lastTargetUUID)

    if not lastTarget then
        sender:sendRichMessage("<red>Player not found.")
        return
    end

    if #args < 1 then
        sender:sendRichMessage("<red>Usage: /r <message>")
        return
    end

    local message = table.concat(args, " ")

    sendPrivateMessage(sender, lastTarget:getName(), message)
end, {
    name = "r",
    description = "Reply to the last player messaged",
    usage = "/r <message>"
})
