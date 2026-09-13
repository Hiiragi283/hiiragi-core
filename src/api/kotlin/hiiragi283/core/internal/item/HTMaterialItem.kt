package hiiragi283.core.internal.item

import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.getOrDefault
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
class HTMaterialItem(private val material: HTMaterial, properties: Properties) : Item(properties) {
    override fun getCreatorModId(itemStack: ItemStack): String = material.getOrDefault(HTMaterialPropertyKeys.ORIGIN_MOD_ID)
}
