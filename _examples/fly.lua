script.registerSimpleCommand(function(sender, args)
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