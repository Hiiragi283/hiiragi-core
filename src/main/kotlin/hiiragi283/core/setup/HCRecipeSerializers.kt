package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.recipe.HTDryingRecipe
import hiiragi283.core.common.recipe.HTSingleItemRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import net.minecraft.world.item.crafting.RecipeSerializer

object HCRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val DRYING: RecipeSerializer<HTDryingRecipe> = REGISTER.registerSerializer(HTConst.DRYING, HTSingleItemRecipe.codec(::HTDryingRecipe))
}
