@file:Suppress("DEPRECATION")

package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * この[ブロック][this]を[HTBlockHolderLike]に変換します。
 * @param BLOCK [Block]を継承したクラス
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
fun <BLOCK : Block> BLOCK.toHolderLike(): HTBlockHolderLike<BLOCK, *> = object : HTBlockHolderLike.Delegated<BLOCK, Item> {
    override fun getBlockHolder(): Holder<Block> = this@toHolderLike.builtInRegistryHolder()

    override fun asBlock(): BLOCK = this@toHolderLike

    override fun getItemHolder(): Holder<Item> = asItem().builtInRegistryHolder()

    override fun asItem(): Item = asBlock().asItem()
}
