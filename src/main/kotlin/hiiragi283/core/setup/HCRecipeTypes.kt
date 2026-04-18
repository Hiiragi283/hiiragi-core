package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import net.minecraft.world.item.crafting.RecipeType
import java.util.function.Supplier

object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    //    Basic    //

    @JvmField
    val CHARGING: Supplier<RecipeType<HCChargingRecipe>> = REGISTER.registerType(HTConst.CHARGING)

    @JvmField
    val CRUSHING: Supplier<RecipeType<HTSingleMultiOutputRecipe.Serializable>> = REGISTER.registerType(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: Supplier<RecipeType<HCExplodingRecipe>> = REGISTER.registerType(HTConst.EXPLODING)

    @JvmField
    val FORGING: Supplier<RecipeType<HTDoubleMultiOutputRecipe.Serializable>> = REGISTER.registerType(HTConst.FORGING)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: Supplier<RecipeType<HTTankEmptyingRecipe.Serializable>> = REGISTER.registerType(HTConst.EMPTYING)

    @JvmField
    val FILLING: Supplier<RecipeType<HTTankFillingRecipe.Serializable>> = REGISTER.registerType(HTConst.FILLING)
}
