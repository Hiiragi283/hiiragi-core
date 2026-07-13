package hiiragi283.core.api.registry

import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.SupplierWithKey
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

/**
 * シンプルな[HTBasicDeferredBlockAndItem]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
typealias HTSimpleDeferredBlockAndItem = HTBasicDeferredBlockAndItem<Block>

/**
 * [HTBlockItem]に基づいた[HTDeferredBlockAndItem]のエイリアスです。
 * @param BLOCK ブロックのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
typealias HTBasicDeferredBlockAndItem<BLOCK> = HTDeferredBlockAndItem<BLOCK, HTBlockItem<BLOCK>>

/**
 * [ブロック][Block]と[アイテム][Item]の両方をもつ[HTDeferredHolder]の補助クラスです。
 * @param BLOCK ブロックのクラス
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
data class HTDeferredBlockAndItem<out BLOCK : Block, out ITEM : Item>(val blockHolder: HTDeferredBlock<BLOCK>, val itemHolder: HTDeferredItem<ITEM>) :
    SupplierWithKey<Block, BLOCK>,
    HTIdLike.Translatable by itemHolder,
    ItemLike by itemHolder {
    constructor(id: ResourceLocation) : this(HTDeferredBlock(id), HTDeferredItem(id))

    override fun get(): BLOCK = blockHolder.get()

    override fun getKey(): ResourceKey<Block> = blockHolder.key

    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack = itemHolder.toStack(count, patch)
}
