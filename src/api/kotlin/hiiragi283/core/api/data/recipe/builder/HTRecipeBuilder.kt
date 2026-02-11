package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.data.holder.HTConditionHolder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[Recipe]のビルダークラスです。
 * @param prefix レシピIDに使われる前置詞
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTRecipeBuilder(private val prefix: String) {
    //    Conditions    //

    /**
     * [ICondition]を保持するインスタンス
     */
    val conditions = HTConditionHolder()

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
     */
    fun save(recipeOutput: RecipeOutput) {
        recipeOutput.accept(recipeId.id.withPrefix("$prefix/"), createRecipe(), null, *conditions.toArray())
    }

    /**
     * デフォルトのIDを取得します。
     */
    protected abstract fun getPrimalId(): ResourceLocation

    /**
     * 生成されるレシピを作成します。
     */
    protected abstract fun createRecipe(): Recipe<*>
}
