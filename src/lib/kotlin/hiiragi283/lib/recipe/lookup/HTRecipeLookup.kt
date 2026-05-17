package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.recipe.HTRecipeHolder
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.context.ContextMap

/**
 * レシピの一覧を提供するインターフェースです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
fun interface HTRecipeLookup<out RECIPE> {
    /**
     * 指定した[context]からレシピの一覧を取得します。
     * @return [HTRecipeHolder]の[Sequence]
     */
    fun getAllRecipes(contextMap: ContextMap): Sequence<HTRecipeHolder<RECIPE>>

    /**
     * 指定した[level]から，[predicate]に一致するレシピを取得します。
     * @return [predicate]に一致するレシピがない場合は`null`
     */
    fun findFirst(level: ServerLevel, predicate: (RECIPE) -> Boolean): HTRecipeHolder<RECIPE>? = HTRecipeLookupContext.create(level).let(::getAllRecipes).firstOrNull { it.recipe.let(predicate) }
}
