package hiiragi283.core.setup

import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup

data object HCRecipeLookups {
    @JvmStatic
    val CHARGING: HTRecipeLookup<HCChargingRecipe> = HTVanillaRecipeLookup(HCRecipeTypes.CHARGING)

    @JvmStatic
    val EXPLODING: HTRecipeLookup<HCExplodingRecipe> = HTVanillaRecipeLookup(HCRecipeTypes.EXPLODING)

    @JvmStatic
    fun init() {
    }
}
