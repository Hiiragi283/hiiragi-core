package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.common.recipe.HCExplodingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class HCExplodingRecipeBuilder : HTRecipeBuilder(HTConst.EXPLODING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HCExplodingRecipeBuilder.() -> Unit) {
            HCExplodingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var ingredient: Ingredient by HTDelegates.onceInitialize()
    var result: HTChancedItemResult by HTDelegates.onceInitialize()

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HCExplodingRecipe = HCExplodingRecipe(ingredient, result)
}
