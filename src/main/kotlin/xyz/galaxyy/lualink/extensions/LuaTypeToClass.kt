import org.luaj.vm2.LuaValue

fun LuaValue.typeToClass(): Class<*> {
    return when {
        this.isnumber() -> Double::class.java
        this.isstring() -> String::class.java
        this.isboolean() -> Boolean::class.java
        this.istable() -> Map::class.java
        else -> throw IllegalArgumentException("Unsupported Lua type")
    }
}
