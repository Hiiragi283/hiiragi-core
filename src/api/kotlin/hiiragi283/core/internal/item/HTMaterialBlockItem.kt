package hiiragi283.core.internal.item

import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.getOrDefault
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

class HTMaterialBlockItem(private val getter: HTPropertyGetter, block: Block, properties: Properties) : BlockItem(block, properties) {
    override fun getCreatorModId(itemStack: ItemStack): String = getter.getOrDefault(HTMaterialPropertyKeys.ORIGIN_MOD_ID)
}
