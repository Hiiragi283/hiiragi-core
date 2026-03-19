package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.registry.HTDeferredRecipeBookCategoryRegister
import net.minecraft.world.item.crafting.RecipeBookCategory

data object HCRecipeBookCategories {
    @JvmField
    val REGISTER = HTDeferredRecipeBookCategoryRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val CHARGING: RecipeBookCategory = REGISTER.register(HTConst.CHARGING)
}
