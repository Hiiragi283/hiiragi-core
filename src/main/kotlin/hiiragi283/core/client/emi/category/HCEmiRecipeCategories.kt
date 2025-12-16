package hiiragi283.core.client.emi.category

import hiiragi283.core.api.HTConst
import hiiragi283.core.setup.HCBlocks
import net.minecraft.world.item.Items

object HCEmiRecipeCategories {
    @JvmField
    val DRYING = HCEmiRecipeCategory(HTConst.DRYING, HCBlocks.DRYING_LACK)

    @JvmField
    val FROSTING = HCEmiRecipeCategory(HTConst.FROSTING, Items.PACKED_ICE)
}
