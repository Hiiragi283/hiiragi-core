package hiiragi283.core.setup

import hiiragi283.core.api.HCConstants
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.HTTankFillingRecipe
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.lib.recipe.lookup.HTCompoundRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup
import hiiragi283.lib.recipe.lookup.fromRecipeType

data object HCRecipeLookups {
    //    In World    //

    @JvmStatic
    val CHARGING: HTRecipeLookup<HCChargingRecipe> = HTVanillaRecipeLookup(HCRecipeTypes.CHARGING)

    @JvmStatic
    val EXPLODING: HTRecipeLookup<HCExplodingRecipe> = HTVanillaRecipeLookup(HCRecipeTypes.EXPLODING)

    //    Tank Interaction    //

    @JvmStatic
    val EMPTYING: HTCompoundRecipeLookup<HTTankEmptyingRecipe> = create(HCConstants.EMPTYING)

    @JvmStatic
    val FILLING: HTCompoundRecipeLookup<HTTankFillingRecipe> = create(HCConstants.FILLING)

    @JvmStatic
    fun <RECIPE : Any> create(name: String): HTCompoundRecipeLookup<RECIPE> = HTCompoundRecipeLookup.create(HiiragiCoreAPI.id(name))

    //    Initialization    //

    @JvmStatic
    fun init() {
        EMPTYING.fromRecipeType(HCRecipeTypes.EMPTYING.get()) { it }

        FILLING.fromRecipeType(HCRecipeTypes.FILLING.get()) { it }
    }
}
