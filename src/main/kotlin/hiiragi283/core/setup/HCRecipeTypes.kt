package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.HTDoubleItemToMultiOutputRecipe
import hiiragi283.core.api.recipe.HTItemToItemRecipe
import hiiragi283.core.api.recipe.HTItemToMultiOutputRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import net.minecraft.world.item.crafting.RecipeType
import java.util.function.Supplier

object HCRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val CHARGING: Supplier<RecipeType<HTItemToItemRecipe.Serializable>> = REGISTER.registerType(HTConst.CHARGING)

    @JvmField
    val CRUSHING: Supplier<RecipeType<HTItemToMultiOutputRecipe.Serializable>> = REGISTER.registerType(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: Supplier<RecipeType<HCExplodingRecipe>> = REGISTER.registerType(HTConst.EXPLODING)

    @JvmField
    val FORGING: Supplier<RecipeType<HTDoubleItemToMultiOutputRecipe.Serializable>> = REGISTER.registerType(HTConst.FORGING)

    @JvmField
    val MELTING: Supplier<RecipeType<HCMeltingRecipe>> = REGISTER.registerType(HTConst.MELTING)
}
