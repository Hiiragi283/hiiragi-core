package hiiragi283.core.api.registry

import hiiragi283.core.api.item.HTItemLike
import hiiragi283.core.api.resource.BlockItemSupplierWithKey
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.SupplierWithKey
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * シンプルな[HTBasicDeferredBlockAndItem]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
typealias HTSimpleDeferredBlockAndItem = HTBasicDeferredBlockAndItem<Block>

/**
 * [BlockItem]に基づいた[HTDeferredBlockAndItem]のエイリアスです。
 * @param BLOCK ブロックのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
typealias HTBasicDeferredBlockAndItem<BLOCK> = HTDeferredBlockAndItem<BLOCK, BlockItem>

/**
 * [ブロック][Block]と[アイテム][Item]の両方をもつ[HTDeferredHolder]の補助クラスです。
 * @param BLOCK ブロックのクラス
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
data class HTDeferredBlockAndItem<out BLOCK : Block, out ITEM : Item>(val blockHolder: HTDeferredBlock<BLOCK>, val itemHolder: HTDeferredItem<ITEM>) :
    BlockItemSupplierWithKey<BLOCK, ITEM>,
    HTIdLike.Translatable by itemHolder,
    HTItemLike<ITEM> by itemHolder {
    constructor(id: ResourceLocation) : this(HTDeferredBlock(id), HTDeferredItem(id))

    override fun get(): BLOCK = blockHolder.get()

    override fun getKey(): ResourceKey<Block> = blockHolder.key

    override fun getItemSupplier(): SupplierWithKey<Item, ITEM> = itemHolder
}
