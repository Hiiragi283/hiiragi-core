package hiiragi283.lib.material

import hiiragi283.lib.registry.toLike
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.left
import hiiragi283.lib.util.right
import hiiragi283.lib.util.unwrap
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

@JvmInline
value class HTMaterialItemEntry(private val content: Either<SupplierWithId<Block>, SupplierWithId<Item>>) :
    SupplierWithId<ItemLike>,
    ItemLike {
    companion object {
        @JvmStatic
        fun block(block: SupplierWithId<Block>): HTMaterialItemEntry = HTMaterialItemEntry(block.left())

        @JvmStatic
        fun block(block: Block): HTMaterialItemEntry = block(block.toLike())

        @JvmStatic
        fun item(item: SupplierWithId<Item>): HTMaterialItemEntry = HTMaterialItemEntry(item.right())

        @JvmStatic
        fun item(item: Item): HTMaterialItemEntry = item(item.toLike())
    }

    override fun get(): ItemLike = content.unwrap().get()

    override fun getId(): Identifier = content.unwrap().getId()

    override fun asItem(): Item = get().asItem()
}
