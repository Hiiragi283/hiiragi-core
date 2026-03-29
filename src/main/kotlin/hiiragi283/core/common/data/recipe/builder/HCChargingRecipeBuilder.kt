package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.registry.holderLike
import hiiragi283.core.common.recipe.HCChargingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient

class HCChargingRecipeBuilder : HTRecipeBuilder(HTConst.CHARGING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HCChargingRecipeBuilder.() -> Unit) {
            HCChargingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: Ingredient
    lateinit var result: ItemStackTemplate
    var energy: Int = HCChargingRecipe.DEFAULT_ENERGY

    override fun getPrimalId(): Identifier = result.holderLike().getId()

    override fun createRecipe(): HCChargingRecipe = HCChargingRecipe(ingredient, result, energy)
}
