package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.data.recipe.result.HTItemResultCreator
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCMiscRegister
import net.minecraft.world.level.ItemLike

object HTMaterialResultHelper {
    @JvmStatic
    fun item(prefix: HTTagPrefix, material: HTMaterialLike, count: Int = 1): HTItemResult {
        val holder: ItemLike? = HCMiscRegister.materialItems[prefix, material.asMaterialKey()]
            ?: VanillaMaterialKeys.INGREDIENTS[prefix, material.asMaterialKey()]
        return when (holder) {
            null -> HTItemResultCreator.create(prefix, material, count)
            else -> HTItemResultCreator.create(holder, prefix, material, count)
        }
    }
}
