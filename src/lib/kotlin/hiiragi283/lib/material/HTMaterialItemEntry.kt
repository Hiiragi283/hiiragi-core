package hiiragi283.lib.material

import hiiragi283.lib.item.HTItemLike
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.util.Either
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

@JvmInline
value class HTMaterialItemEntry(private val content: Either<SupplierWithId<Block>, SupplierWithId<Item>>) :
    SupplierWithId<ItemLike>,
    HTItemLike<Item> {
    companion object {
        @JvmStatic
        fun block(block: SupplierWithId<Block>): HTMaterialItemEntry = HTMaterialItemEntry(Either.Left(block))

        @JvmStatic
        fun block(block: Block): HTMaterialItemEntry = block(BlockWrapper(block))

        @JvmStatic
        fun item(item: SupplierWithId<Item>): HTMaterialItemEntry = HTMaterialItemEntry(Either.Right(item))

        @JvmStatic
        fun item(item: Item): HTMaterialItemEntry = item(ItemWrapper(item))
    }

    private class BlockWrapper(val block: Block) : SupplierWithId<Block> {
        override fun get(): Block = block

        @Suppress("DEPRECATION")
        override fun getId(): Identifier = block.builtInRegistryHolder().unwrapKey().orElseThrow().identifier()
    }

    private class ItemWrapper(val item: Item) : SupplierWithId<Item> {
        override fun get(): Item = item

        @Suppress("DEPRECATION")
        override fun getId(): Identifier = item.builtInRegistryHolder().unwrapKey().orElseThrow().identifier()
    }

    override fun get(): ItemLike = content.fold(SupplierWithId<Block>::get, SupplierWithId<Item>::get)

    override fun getId(): Identifier = content.fold(SupplierWithId<Block>::getId, SupplierWithId<Item>::getId)

    override fun asItem(): Item = get().asItem()
}
