package hiiragi283.core.setup

import hiiragi283.core.api.HCConstants
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.HTTankFillingRecipe
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.custom.HTPotionArrowFillingRecipe
import hiiragi283.core.common.recipe.custom.HTPotionTankInteraction
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.base.HTItemToChancedItemsRecipe
import hiiragi283.lib.recipe.lookup.HTCompoundRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup
import hiiragi283.lib.recipe.lookup.fromRecipeType
import hiiragi283.lib.resource.toId
import hiiragi283.lib.util.identity

data object HCRecipeLookups {
    //    In World    //

    @JvmStatic
    val CHARGING: HTRecipeLookup<HCChargingRecipe> = HTVanillaRecipeLookup(HCRecipeTypes.CHARGING)

    @JvmStatic
    val CHOPPING: HTCompoundRecipeLookup<HTItemToChancedItemsRecipe> = create(HCConstants.CHOPPING)

    @JvmStatic
    val CRUSHING: HTCompoundRecipeLookup<HTItemToChancedItemsRecipe> = create(HCConstants.CRUSHING)

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
        CHOPPING.fromRecipeType(HCRecipeTypes.CHOPPING, identity())

        CRUSHING.fromRecipeType(HCRecipeTypes.CRUSHING, identity())

        EMPTYING.fromRecipeType(HCRecipeTypes.EMPTYING, identity())
        EMPTYING.addRecipes(HTConstants.MINECRAFT.toId(HCConstants.EMPTYING, "potion") to HTPotionTankInteraction.Emptying)

        FILLING.fromRecipeType(HCRecipeTypes.FILLING, identity())
        FILLING.addRecipes(HTConstants.MINECRAFT.toId(HCConstants.FILLING, "potion") to HTPotionTankInteraction.Filling)
        FILLING.addRecipes(HTConstants.MINECRAFT.toId(HCConstants.FILLING, "potion_arrow") to HTPotionArrowFillingRecipe)
    }
}
