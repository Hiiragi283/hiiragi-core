package hiiragi283.core.client.emi.category

import hiiragi283.core.api.HTConst
import hiiragi283.core.common.item.HTHammerItem
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems

object HCEmiRecipeCategories {
    @JvmField
    val CRUSHING = HCEmiRecipeCategory(HTConst.CRUSHING, HCItems.TOOLS[HTHammerItem.ToolType, HCMaterial.Metals.IRON]!!)

    @JvmField
    val DRYING = HCEmiRecipeCategory(HTConst.DRYING, HCBlocks.DRYING_LACK)
}
