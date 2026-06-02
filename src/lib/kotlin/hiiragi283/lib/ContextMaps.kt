package hiiragi283.lib

import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.right
import net.minecraft.util.context.ContextKey
import net.minecraft.util.context.ContextMap

fun <T : Any> ContextMap.getResult(key: ContextKey<T>): HTTextResult<T> = this.getOptional(key)?.right() ?: HTTextResult("Unbounded context ${key.name()}")
