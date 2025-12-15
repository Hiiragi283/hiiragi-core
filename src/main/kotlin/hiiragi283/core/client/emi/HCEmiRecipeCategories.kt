package hiiragi283.core.client.emi

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiRecipeSorting
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object HCEmiRecipeCategories {
    @JvmField
    val DRYING: EmiRecipeCategory = create(HTConst.DRYING, Items.DRIED_KELP_BLOCK)

    @JvmStatic
    private fun create(path: String, icon: ItemLike): EmiRecipeCategory = EmiRecipeCategory(
        HiiragiCoreAPI.id(path),
        EmiStack.of(icon),
        EmiStack.of(icon),
        EmiRecipeSorting.compareOutputThenInput(),
    )
}
