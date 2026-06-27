package hiiragi283.lib.resource

import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

typealias SimpleBlockItemSupplierWithKey = BlockItemSupplierWithKey<Block, Item>

interface BlockItemSupplierWithKey<out BLOCK : Block, out ITEM : Item> : SupplierWithKey<Block, BLOCK> {
    fun getItemSupplier(): SupplierWithKey<Item, ITEM>
}
