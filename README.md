# LuaLink Plugin

LuaLink is an experimental plugin that provides a basic Lua scripting runtime for Paper-based Minecraft servers. It is designed for small and simple tasks and serves as an alternative to Skript.

## Requirements

To use the LuaLink plugin, you need the following:

- A [Paper](https://papermc.io/) based Minecraft server.
- A basic understanding of Lua scripting.

**Note**: Detailed documentation is coming soon.


<details>
<summary>Examples</summary>

### **For more advanced examples see the [examples](https://github.com/Saturn745/LuaLink/tree/_examples) folder** 

### Hello command
```lua
plugin.onEnable(function()
    plugin.logger.info("Hello world command enabled!!!!!!!! Let's be annoying and make every script spam console when it loads")
end)

plugin.registerSimpleCommand(function(sender, args)
    sender:sendRichMessage("<green>Hello, "..sender:getName())
end, {
    name = "hello",
    description = "Hello world from LuaLink"
})
```

### Welcomer | Event listener

```lua
local function listen()
    plugin.hook("org.bukkit.event.player.PlayerJoinEvent", function(event)
        local player = event:getPlayer()
        player:playSound(player:getLocation(), "entity.firework_rocket.launch", 1.0, 1.0)
        player:sendRichMessage("<green>Welcome back to the server, " .. player:getName() .. "!")
    end)
end

plugin.onEnable(function()
    plugin.logger.info("Welcomer loaded! PS: You don't have to do this in every script, LuaLink already logs when a script is loaded.")
    listen()
end)
```
</details>
