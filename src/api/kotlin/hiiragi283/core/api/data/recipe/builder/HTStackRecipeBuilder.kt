package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.registry.toLike
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe

/**
 * 単一の[ItemStack]を完成品にとるレシピ向けの[HTRecipeBuilder]の拡張クラスです。
 * @param BUILDER [HTStackRecipeBuilder]を継承したクラス
 * @param stack レシピの完成品
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTStackRecipeBuilder.Single
 */
abstract class HTStackRecipeBuilder<BUILDER : HTStackRecipeBuilder<BUILDER>>(prefix: String, protected val stack: ItemStack) :
    HTRecipeBuilder<BUILDER>(prefix) {
    /**
     * 指定した[完成品][output]からレシピを作成します。
     */
    protected abstract fun createRecipe(output: ItemStack): Recipe<*>

    final override fun getPrimalId(): ResourceLocation = stack.itemHolder.toLike().getId()

    final override fun createRecipe(): Recipe<*> = createRecipe(stack)

    //    Single    //

    /**
     * 単一の[Ingredient]から単一の[ItemStack]に変換するレシピ向けの[HTRecipeBuilder]の拡張クラスです。
     * @param BUILDER [Single]を継承したクラス
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    abstract class Single<BUILDER : Single<BUILDER>>(prefix: String, stack: ItemStack) :
        HTStackRecipeBuilder<BUILDER>(prefix, stack),
        HTIngredientRecipeBuilder<BUILDER> {
        protected lateinit var ingredient: Ingredient

        @Suppress("UNCHECKED_CAST")
        final override fun addIngredient(ingredient: Ingredient): BUILDER {
            check(!::ingredient.isInitialized) { "Ingredient has already been initialized!" }
            this.ingredient = ingredient
            return this as BUILDER
        }
    }
}
