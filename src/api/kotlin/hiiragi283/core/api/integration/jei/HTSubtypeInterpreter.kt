package hiiragi283.core.api.integration.jei

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext

fun interface HTSubtypeInterpreter<T> : ISubtypeInterpreter<T> {
    @Deprecated("Deprecated in Java")
    override fun getLegacyStringSubtypeInfo(ingredient: T, context: UidContext): String =
        getSubtypeData(ingredient, context)?.toString() ?: ""
}
