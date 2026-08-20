@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.data.ConditionBuilder
import hiiragi283.core.api.util.HTBuilderMarker
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * Hiiragi Seriesで使用される[Recipe]のビルダークラスです。
 * @param RECIPE 生成するレシピのクラス
 * @param prefix レシピIDに使われる前置詞
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@HTBuilderMarker
abstract class HTRecipeBuilder<out RECIPE : Recipe<*>>(private val prefix: String) {
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
     * レシピ[ID][ResourceLocation]を保持するインスタンス
     */
    val recipeId: RecipeId by lazy(::RecipeId)

    inner class RecipeId {
        /**
         * 保持している[ID][ResourceLocation]
         */
        var id: ResourceLocation = getPrimalId()
            private set

        /**
         * 現在の[ID][ResourceLocation]にプレフィックスを追加します。
         */
        infix fun prefix(prefix: String) {
            id = id.withPrefix(prefix)
        }

        /**
         * 現在の[ID][ResourceLocation]にサフィックスを追加します。
         */
        infix fun suffix(suffix: String) {
            id = id.withSuffix(suffix)
        }

        /**
         * 現在の[ID][ResourceLocation]を[newId]で置換します。
         */
        infix fun replace(newId: ResourceLocation) {
            id = newId
        }
    }

    /**
     * レシピを生成します。
     * @param exporter 生成したレシピの出力先
     */
    open fun save(exporter: HTRecipeExporter) {
        this.save { id: ResourceLocation, recipe: RECIPE -> exporter.accept(id, recipe, conditions) }
    }

    /**
     * 生成したレシピを処理します。
     * @param consumer 生成されたレシピIDとレシピを処理するブロック
     */
    fun <R> save(consumer: (id: ResourceLocation, recipe: RECIPE) -> R): R {
        contract {
            callsInPlace(consumer, InvocationKind.EXACTLY_ONCE)
        }
        return consumer(recipeId.id.withPrefix("$prefix/"), createRecipe())
    }

    /**
     * デフォルトのIDを取得します。
     */
    protected abstract fun getPrimalId(): ResourceLocation

    /**
     * レシピを生成します。
     */
    abstract fun createRecipe(): RECIPE
}
