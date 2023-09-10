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
