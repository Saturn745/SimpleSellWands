package xyz.galaxyy.lualink.lua

import com.github.only52607.luakt.CoerceKotlinToLua
import org.bukkit.Bukkit
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.VarArgFunction
import xyz.galaxyy.lualink.LuaLink

class LuaUtils(private val plugin: LuaLink, private val script: LuaScript) : LuaTable() {
    init {
        this.set("instanceOf", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                if (args == null || args.narg() != 2) {
                    throw IllegalArgumentException("instanceOf expects 2 arguments: object, class")
                }

                val obj = args.arg(1).checkuserdata()
                val clazzName = args.checkjstring(2)

                // Get the class of the object
                val objClass = obj.javaClass

                // Get the class of the specified class name
                val specifiedClass = try {
                    Class.forName(clazzName)
                } catch (e: ClassNotFoundException) {
                    throw IllegalArgumentException("Class not found: $clazzName")
                }

                // Check if the object's class is assignable from the specified class
                return LuaValue.valueOf(specifiedClass.isAssignableFrom(objClass))
            }
        })

        this.set("runTask", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                if (args == null || args.narg() != 1) {
                    throw IllegalArgumentException("runTask expects 1 argument: callback")
                }

                val callback = args.arg(2).checkfunction()

                val task = Bukkit.getScheduler().runTask(plugin, Runnable {
                    callback.call()
                })

                script.tasks.add(task.taskId)

                return CoerceKotlinToLua.coerce(task)
            }
        })

        this.set("runTaskAsynchronously", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                if (args == null || args.narg() != 2) {
                    throw IllegalArgumentException("runTaskAsynchronously expects 1 argument: callback")
                }

                val callback = args.arg(1).checkfunction()

                val task = Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
                    callback.call()
                })

                script.tasks.add(task.taskId)

                return CoerceKotlinToLua.coerce(task)
            }
        })

        this.set("scheduleSyncDelayedTask", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                if (args == null || args.narg() != 2) {
                    throw IllegalArgumentException("scheduleSyncDelayedTask expects 2 arguments: callback, delay")
                }

                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checkint()

                val task = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
                    callback.call()
                }, delay.toLong())

                script.tasks.add(task)

                return LuaValue.valueOf(task)
            }
        })

        this.set("scheduleSyncRepeatingTask", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                if (args == null || args.narg() != 3) {
                    throw IllegalArgumentException("scheduleSyncRepeatingTask expects 3 arguments: callback, delay, period")
                }

                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checkint()
                val period = args.arg(3).checkint()

                val task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
                    callback.call()
                }, delay.toLong(), period.toLong())

                script.tasks.add(task)

                return LuaValue.valueOf(task)
            }
        })

        this.set("runTaskAsynchronously", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                if (args == null || args.narg() != 1) {
                    throw IllegalArgumentException("runTaskAsynchronously expects 1 argument: callback")
                }

                val callback = args.arg(1).checkfunction()

                val task = Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
                    callback.call()
                })

                script.tasks.add(task.taskId)

                return CoerceKotlinToLua.coerce(task)
            }
        })

        this.set("runTaskLaterAsynchronously", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                if (args == null || args.narg() != 2) {
                    throw IllegalArgumentException("runTaskLaterAsynchronously expects 2 arguments: callback, delay")
                }

                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checkint()

                val task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Runnable  {
                    callback.call()
                }, delay.toLong())

                script.tasks.add(task.taskId)

                return CoerceKotlinToLua.coerce(task)
            }
        })

        this.set("runTaskLater", object: VarArgFunction() {
            override fun invoke(args: Varargs?): Varargs {
                if (args == null || args.narg() != 2) {
                    throw IllegalArgumentException("runTaskLater expects 2 arguments: callback, delay")
                }

                val callback = args.arg(1).checkfunction()
                val delay = args.arg(2).checkint()

                val task = Bukkit.getScheduler().runTaskLater(plugin, Runnable  {
                    callback.call()
                }, delay.toLong())

                script.tasks.add(task.taskId)

                return CoerceKotlinToLua.coerce(task)
            }
        })

        this.set("cancelTask", object: OneArgFunction() {
            override fun call(arg: LuaValue?): LuaValue {
                if (arg == null || !arg.isint()) {
                    throw IllegalArgumentException("cancelTask expects 1 argument: taskId")
                }

                val taskId = arg.checkint()

                Bukkit.getScheduler().cancelTask(taskId)

                if (script.tasks.contains(taskId)) {
                    script.tasks.remove(taskId)
                }

                return LuaValue.NIL
            }
        })

    }
}