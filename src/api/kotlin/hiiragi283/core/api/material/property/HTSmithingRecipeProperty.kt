package hiiragi283.core.api.material.property

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTItemHolderLike

/**
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
@JvmRecord
data class HTSmithingRecipeProperty(val template: HTItemHolderLike<*>, val base: HTMaterialKey, val allowCrafting: Boolean)
