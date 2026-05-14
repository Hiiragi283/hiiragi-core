package hiiragi283.core.data.recipe

import hiiragi283.core.api.HCConstants
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.lib.data.recipe.HTRecipeBuilder
import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Ingredient

class HCChargingRecipeBuilder : HTRecipeBuilder<HCChargingRecipe>(HCConstants.CHARGING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HCChargingRecipeBuilder.() -> Unit): HCChargingRecipeBuilder = HCChargingRecipeBuilder().apply(builderAction)
    }

    var ingredient: Ingredient by HTDelegates.onceInitialize()
    var result: HTChancedItemResult by HTDelegates.onceInitialize()
    var energy: Int = HCChargingRecipe.DEFAULT_ENERGY

    override fun getPrimalId(): Identifier = result.getId()

    override fun createRecipe(): HCChargingRecipe = HCChargingRecipe(ingredient, result, energy)
}
