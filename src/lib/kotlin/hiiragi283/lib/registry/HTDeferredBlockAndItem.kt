package hiiragi283.lib.registry

import hiiragi283.lib.item.HTBlockItem
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.SupplierWithId
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

/**
 * シンプルな[HTBasicDeferredBlockAndItem]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTSimpleDeferredBlockAndItem = HTBasicDeferredBlockAndItem<Block>

/**
 * [HTBlockItem]に基づいた[HTDeferredBlockAndItem]のエイリアスです。
 * @param BLOCK ブロックのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTBasicDeferredBlockAndItem<BLOCK> = HTDeferredBlockAndItem<BLOCK, HTBlockItem<BLOCK>>

/**
 * [ブロック][Block]と[アイテム][Item]の両方をもつ[HTDeferredHolder]の補助クラスです。
 * @param BLOCK ブロックのクラス
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data class HTDeferredBlockAndItem<out BLOCK : Block, out ITEM : Item>(val blockHolder: HTDeferredBlock<BLOCK>, val itemHolder: HTDeferredItem<ITEM>) :
    SupplierWithId<BLOCK>,
    HTIdLike.Translatable by itemHolder,
    ItemLike by itemHolder {
    override fun get(): BLOCK = blockHolder.get()

    /**
     * 新しい[ItemStackTemplate]のインスタンスを作成します。
     */
    fun toTemplate(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStackTemplate = itemHolder.toTemplate(count, patch)

    /**
     * 新しい[ItemStack]のインスタンスを作成します。
     */
    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack = itemHolder.toStack(count, patch)
}
