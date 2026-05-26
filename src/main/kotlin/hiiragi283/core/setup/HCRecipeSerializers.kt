package hiiragi283.core.setup

import hiiragi283.core.api.HCConstants
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCChoppingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.lib.registry.HTDeferredRecipeSerializerRegister
import net.minecraft.world.item.crafting.RecipeSerializer

data object HCRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(HiiragiCoreAPI.MOD_ID)

    //    In World    //

    @JvmField
    val CHARGING: RecipeSerializer<HCChargingRecipe> = REGISTER.registerSerializer(HCConstants.CHARGING, HCChargingRecipe.CODEC)

    @JvmField
    val CHOPPING: RecipeSerializer<HCChoppingRecipe> = REGISTER.registerSerializer(HCConstants.CHOPPING, HCChoppingRecipe.CODEC)

    @JvmField
    val EXPLODING: RecipeSerializer<HCExplodingRecipe> = REGISTER.registerSerializer(HCConstants.EXPLODING, HCExplodingRecipe.CODEC)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: RecipeSerializer<HCTankEmptyingRecipe> = REGISTER.registerSerializer(HCConstants.EMPTYING, HCTankEmptyingRecipe.CODEC)

    @JvmField
    val FILLING: RecipeSerializer<HCTankFillingRecipe> = REGISTER.registerSerializer(HCConstants.FILLING, HCTankFillingRecipe.CODEC)
}
