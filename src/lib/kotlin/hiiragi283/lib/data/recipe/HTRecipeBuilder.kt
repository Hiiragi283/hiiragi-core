@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.data.ConditionBuilder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.util.HTBuilderMarker
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[Recipe]のビルダークラスです。
 * @param prefix レシピIDに使われる前置詞
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@HTBuilderMarker
abstract class HTRecipeBuilder<out RECIPE : Recipe<*>>(private val prefix: String) {
    fun commonInfo(showNotification: Boolean): Recipe.CommonInfo = Recipe.CommonInfo(showNotification)

    //    Conditions    //

    /**
     * [ICondition]を保持するインスタンス
     */
    @PublishedApi
    internal val conditions: MutableList<ICondition> = mutableListOf()

    inline fun condition(builderAction: ConditionBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ConditionBuilder(conditions).apply(builderAction)
    }

    //    Save    //

    /**
     * レシピ[ID][Identifier]を保持するインスタンス
     */
    val recipeId: RecipeId by lazy(::RecipeId)

    inner class RecipeId {
        /**
         * 保持している[ID][Identifier]
         */
        var id: Identifier = getPrimalId()
            private set

        /**
         * 現在の[ID][Identifier]にプレフィックスを追加します。
         */
        infix fun prefix(prefix: String) {
            id = id.withPrefix(prefix)
        }

        /**
         * 現在の[ID][Identifier]にサフィックスを追加します。
         */
        infix fun suffix(suffix: String) {
            id = id.withSuffix(suffix)
        }

        /**
         * 現在の[ID][Identifier]を[newId]で置換します。
         */
        infix fun replace(newId: Identifier) {
            id = newId
        }
    }

    /**
     * レシピを生成します。
     */
    open fun save(exporter: HTRecipeExporter) {
        this.save { id: Identifier, recipe: RECIPE ->
            exporter.accept(RecipeKey(id), recipe, conditions)
        }
    }

    fun save(consumer: (id: Identifier, recipe: RECIPE) -> Unit) {
        contract {
            callsInPlace(consumer, InvocationKind.EXACTLY_ONCE)
        }
        consumer(
            recipeId.id.withPrefix("$prefix/"),
            createRecipe(),
        )
    }

    /**
     * デフォルトのIDを取得します。
     */
    protected abstract fun getPrimalId(): Identifier

    /**
     * 生成されるレシピを作成します。
     */
    protected abstract fun createRecipe(): RECIPE
}
