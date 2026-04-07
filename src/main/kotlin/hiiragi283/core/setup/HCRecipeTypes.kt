package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTSingleItemRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import net.minecraft.world.item.crafting.RecipeType
import java.util.function.Supplier

object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val CHARGING: Supplier<RecipeType<HTSingleItemRecipe.Serializable>> = REGISTER.registerType(HTConst.CHARGING)

    @JvmField
    val CRUSHING: Supplier<RecipeType<HTSingleMultiOutputRecipe.Serializable>> = REGISTER.registerType(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: Supplier<RecipeType<HCExplodingRecipe>> = REGISTER.registerType(HTConst.EXPLODING)

    @JvmField
    val FORGING: Supplier<RecipeType<HTDoubleMultiOutputRecipe.Serializable>> = REGISTER.registerType(HTConst.FORGING)

    @JvmField
    val MELTING: Supplier<RecipeType<HCMeltingRecipe>> = REGISTER.registerType(HTConst.MELTING)
}
