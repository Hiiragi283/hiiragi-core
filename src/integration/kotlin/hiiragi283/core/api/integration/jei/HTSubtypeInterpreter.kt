package hiiragi283.core.api.integration.jei

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[ISubtypeInterpreter]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
fun interface HTSubtypeInterpreter<T : Any> : ISubtypeInterpreter<T> {
    @Deprecated("Deprecated in Java")
    override fun getLegacyStringSubtypeInfo(ingredient: T, context: UidContext): String =
        getSubtypeData(ingredient, context)?.toString() ?: ""
}
