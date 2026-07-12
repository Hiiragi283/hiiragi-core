package hiiragi283.lib.material

import hiiragi283.lib.registry.HTDeferredBlock
import hiiragi283.lib.registry.HTDeferredItem
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.SimpleSupplierWithKey
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.references.BlockItemId
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

/**
 * ブロックまたはアイテムを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed interface HTMaterialItemEntry :
    HTIdLike,
    ItemLike {
    @Suppress("DEPRECATION")
    fun getItemHolder(): Holder<Item> = asItem().builtInRegistryHolder()

    /**
     * @since 26.1.1
     */
    fun toTemplate(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStackTemplate = ItemStackTemplate(asItem(), count, patch)

    /**
     * @since 26.1.1
     */
    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack = ItemStack(getItemHolder(), count, patch)

    class BlockEntry(private val block: SimpleSupplierWithKey<Block>) :
        HTMaterialItemEntry,
        SimpleSupplierWithKey<Block> by block {
        constructor(id: BlockItemId) : this(id.block())

        constructor(key: ResourceKey<Block>) : this(HTDeferredBlock(key))

        override fun asItem(): Item = block.get().asItem()
    }

    class ItemEntry(private val item: SimpleSupplierWithKey<Item>) :
        HTMaterialItemEntry,
        SimpleSupplierWithKey<Item> by item {
        constructor(id: BlockItemId) : this(id.item())

        constructor(key: ResourceKey<Item>) : this(HTDeferredItem(key))

        override fun asItem(): Item = item.get()
    }
}
