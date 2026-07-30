package hiiragi283.core.internal.item

import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.getOrDefault
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
class HTMaterialBlockItem(private val material: HTMaterial, block: Block, properties: Properties) : BlockItem(block, properties) {
    override fun getCreatorModId(itemStack: ItemStack): String = material.getOrDefault(HTMaterialPropertyKeys.ORIGIN_MOD_ID)
}
