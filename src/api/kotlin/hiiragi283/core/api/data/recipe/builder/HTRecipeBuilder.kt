package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.data.holder.HTConditionHolder
import hiiragi283.core.api.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.Identifier
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
    fun save(recipeOutput: RecipeOutput) {
        recipeOutput.accept(
            Registries.RECIPE.createKey(recipeId.id.withPrefix("$prefix/")),
            createRecipe(),
            null,
            *conditions.toArray(),
        )
    }

    /**
     * デフォルトのIDを取得します。
     */
    protected abstract fun getPrimalId(): Identifier

    /**
     * 生成されるレシピを作成します。
     */
    protected abstract fun createRecipe(): Recipe<*>
}
