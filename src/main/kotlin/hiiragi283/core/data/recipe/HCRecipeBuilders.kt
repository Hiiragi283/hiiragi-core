package hiiragi283.core.data.recipe

import hiiragi283.core.api.HCConstants
import hiiragi283.core.common.recipe.HCChoppingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.lib.data.recipe.HTItemToChancedItemsRecipeBuilder

data object HCRecipeBuilders {
    @JvmStatic
    inline fun chopping(builderAction: HTItemToChancedItemsRecipeBuilder<HCChoppingRecipe>.() -> Unit): HTItemToChancedItemsRecipeBuilder<HCChoppingRecipe> = HTItemToChancedItemsRecipeBuilder(HCConstants.CHOPPING, ::HCChoppingRecipe).apply(builderAction)

    @JvmStatic
    inline fun crushing(builderAction: HTItemToChancedItemsRecipeBuilder<HCCrushingRecipe>.() -> Unit): HTItemToChancedItemsRecipeBuilder<HCCrushingRecipe> = HTItemToChancedItemsRecipeBuilder(HCConstants.CRUSHING, ::HCCrushingRecipe).apply(builderAction)
}
