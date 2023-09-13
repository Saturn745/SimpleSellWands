-- Example showing how to import java classes and use them in Lua.
local Component = import "net.kyori.adventure.text.Component"
script.registerSimpleCommand(function(sender, args)
    local msg = Component:text("Hello, world!")
    sender:sendMessage(msg)
end, {
    name = "component",
})