package xyz.galaxyy.lualink.lua.misc

import com.github.only52607.luakt.extension.forEach
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.CoerceLuaToJava
import typeToClass

class LuaNewInstance : TwoArgFunction() {
    override fun call(lclazzPath: LuaValue?, lconstructorArgs: LuaValue?): LuaValue {
        if (lclazzPath == null || lconstructorArgs == null) {
            throw IllegalArgumentException("newInstance expects 2 arguments: string, string")
        }
        try {
            val clazzPath: String = lclazzPath.checkjstring()
            val constructorArgs: LuaTable = lconstructorArgs.checktable()

            val clazz = Class.forName(clazzPath)

            val constructorArgTypes = mutableListOf<Class<*>>()
            val constructorArgsList = mutableListOf<Any?>()

            constructorArgs.forEach { _, value ->
                val argType = value.typeToClass()
                constructorArgTypes.add(argType)

                val coercedValue = CoerceLuaToJava.coerce(value, argType)
                constructorArgsList.add(coercedValue)
            }

            val constructor = clazz.getConstructor(*constructorArgTypes.toTypedArray())
            val instance = constructor.newInstance(*constructorArgsList.toTypedArray())
            return CoerceJavaToLua.coerce(instance)


        } catch (e: Exception) {
            throw IllegalArgumentException("newInstance failed: ${e.message}")
        }

    }
}