package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.data.recipe.result.HTItemResultCreator
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems

object HCMaterialResultHelper {
    @JvmStatic
    fun item(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): HTItemResult {
        val holder: HTItemHolderLike<*>? = HCItems.MATERIALS[prefix, material] ?: VanillaMaterialKeys.INGREDIENTS[prefix, material]
        return when (holder) {
            null -> HTItemResultCreator.create(prefix, material, count)
            else -> HTItemResultCreator.create(holder, prefix, material, count)
        }
    }
}
