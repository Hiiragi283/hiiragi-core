package hiiragi283.lib.data.recipe

import hiiragi283.lib.data.HTConditionHolder
import hiiragi283.lib.recipe.RecipeKey
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
abstract class HTRecipeBuilder<out RECIPE : Recipe<*>>(private val prefix: String) {
    fun commonInfo(showNotification: Boolean): Recipe.CommonInfo = Recipe.CommonInfo(showNotification)

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
    open fun save(recipeOutput: RecipeOutput) {
        this.save { id: Identifier, recipe: RECIPE ->
            recipeOutput.accept(RecipeKey(id), recipe, null, *conditions.toArray())
        }
    }

    fun save(consumer: (id: Identifier, recipe: RECIPE) -> Unit) {
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
