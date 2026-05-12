package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.impl.data.recipe.builder.HTStackRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import java.util.function.IntUnaryOperator
import net.minecraft.world.item.crafting.Ingredient

class HTCookingRecipeBuilder(
    private val factory: AbstractCookingRecipe.Factory<*>,
    private val timeOperator: IntUnaryOperator,
    prefix: String,
) : HTStackRecipeBuilder(prefix) {
    companion object {
        @JvmStatic
        inline fun smelting(output: RecipeOutput, builderAction: HTCookingRecipeBuilder.() -> Unit) {
            HTCookingRecipeBuilder(::SmeltingRecipe, IntUnaryOperator.identity(), HTConst.SMELTING)
                .apply(builderAction)
                .save(output)
        }

        @JvmStatic
        inline fun blasting(output: RecipeOutput, builderAction: HTCookingRecipeBuilder.() -> Unit) {
            HTCookingRecipeBuilder(::BlastingRecipe, IntUnaryOperator.identity(), HTConst.BLASTING)
                .apply(builderAction)
                .save(output)
        }

        @JvmStatic
        inline fun smoking(output: RecipeOutput, builderAction: HTCookingRecipeBuilder.() -> Unit) {
            HTCookingRecipeBuilder(::SmokingRecipe, IntUnaryOperator.identity(), HTConst.SMOKING)
                .apply(builderAction)
                .save(output)
        }

        @JvmStatic
        inline fun smeltingAndBlasting(output: RecipeOutput, builderAction: HTCookingRecipeBuilder.() -> Unit) {
            smelting(output, builderAction)
            HTCookingRecipeBuilder(::BlastingRecipe, { it / 2 }, HTConst.BLASTING)
                .apply(builderAction)
                .save(output)
        }

        @JvmStatic
        inline fun smeltingAndSmoking(output: RecipeOutput, builderAction: HTCookingRecipeBuilder.() -> Unit) {
            smelting(output, builderAction)
            HTCookingRecipeBuilder(::SmokingRecipe, { it / 2 }, HTConst.SMOKING)
                .apply(builderAction)
                .save(output)
        }
    }

    var group: String? = null
    var category: CookingBookCategory = CookingBookCategory.MISC
    var ingredient: Ingredient by HTDelegates.onceInitialize()
    var exp: Float = 0f
    var time: Int = 20 * 10

    override fun createRecipe(): AbstractCookingRecipe = factory.create(
        group ?: "",
        category,
        ingredient,
        resultStack.stack,
        exp,
        timeOperator.applyAsInt(time),
    )
}
