package hiiragi283.core.api.registry

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * [ブロック][Block]向けの[HTIdLike]の拡張インターフェースです。
 * @param BLOCK ブロックのクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
interface HTBlockHolderLike<BLOCK : Block> :
    HTIdLike,
    HTHasTranslationKey,
    HTHasText {
    /**
     * 保持している[ブロック][BLOCK]を取得します。
     */
    fun asBlock(): BLOCK

    /**
     * 保持しているブロックの[Holder]を取得します。
     */
    fun getBlockHolder(): Holder<Block>

    companion object {
        /**
         * [Holder]に基づいた[HTBlockHolderLike]の[BiCodec]
         */
        @JvmField
        val HOLDER_CODEC: BiCodec<RegistryFriendlyByteBuf, HTBlockHolderLike<*>> = VanillaBiCodecs
            .holder(Registries.BLOCK)
            .xmap(Holder<Block>::value.andThen(::of), HTBlockHolderLike<*>::getBlockHolder)

        /**
         * 指定した[holder]から[HTBlockHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun of(holder: Holder<Block>): HTBlockHolderLike<Block> = object : Simple<Block> {
            override fun asBlock(): Block = getBlockHolder().value()

            override fun getBlockHolder(): Holder<Block> = holder
        }

        /**
         * 指定した[block]から[HTBlockHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun <BLOCK : Block> of(block: BLOCK): HTBlockHolderLike<BLOCK> = object : Simple<BLOCK> {
            override fun asBlock(): BLOCK = block

            @Suppress("DEPRECATION")
            override fun getBlockHolder(): Holder<Block> = block.builtInRegistryHolder()
        }

        /**
         * 指定した[block]を[HTItemHolderLike]に変換します。
         */
        @JvmStatic
        fun wrap(block: HTBlockHolderLike<*>): HTItemHolderLike<*> = object : HTItemHolderLike.Simple<Item> {
            override fun asItem(): Item = block.asBlock().asItem()

            @Suppress("DEPRECATION")
            override fun getItemHolder(): Holder<Item> = asItem().builtInRegistryHolder()
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 0.11.0
     */
    interface Simple<BLOCK : Block> : HTBlockHolderLike<BLOCK> {
        override fun getId(): ResourceLocation = getBlockHolder().toLike().getId()

        override val translationKey: String get() = asBlock().descriptionId

        override fun getText(): Component = asBlock().name
    }
}
