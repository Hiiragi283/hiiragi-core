package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * ブロックに対応した[HTItemHolderLike]の拡張インターフェースです。
 * @param BLOCK ブロックのクラス
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTBlockHolderLike<BLOCK : Block, ITEM : Item> : HTItemHolderLike<ITEM> {
    fun getBlockHolder(): Holder<Block>

    fun getBlockKey(): ResourceKey<Block> = getBlockHolder().unwrapKey().orElseThrow()

    fun asBlock(): BLOCK

    /**
     * @since 0.8.0
     */
    interface Delegated<BLOCK : Block, ITEM : Item> : HTBlockHolderLike<BLOCK, ITEM> {
        override fun getId(): ResourceLocation = getBlockKey().location()

        override val translationKey: String get() = asBlock().descriptionId

        override fun getText(): Component = asBlock().name
    }
}
