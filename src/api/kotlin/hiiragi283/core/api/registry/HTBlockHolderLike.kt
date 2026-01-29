package hiiragi283.core.api.registry

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
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
    companion object {
        @JvmField
        val KEY_CODEC: BiCodec<ByteBuf, HTBlockHolderLike<*, *>> = VanillaBiCodecs
            .resourceKey(Registries.BLOCK)
            .xmap(::of, HTBlockHolderLike<*, *>::getBlockKey)

        @JvmField
        val HOLDER_CODEC: BiCodec<RegistryFriendlyByteBuf, HTBlockHolderLike<*, *>> = VanillaBiCodecs
            .holder(Registries.BLOCK)
            .xmap(Holder<Block>::value.andThen(::of), HTBlockHolderLike<*, *>::getBlockHolder)

        /**
         * 指定した[id]から[HTBlockHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun of(id: ResourceLocation): HTBlockHolderLike<*, *> = of(Registries.BLOCK.createKey(id))

        /**
         * 指定した[key]から[HTBlockHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun of(key: ResourceKey<Block>): HTBlockHolderLike<*, *> = Simple(Either.Left(key))

        /**
         * 指定した[block]から[HTBlockHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun of(block: Block): HTBlockHolderLike<*, *> = Simple(Either.Right(block))
    }

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

    /**
     * @suppress
     */
    @JvmInline
    private value class Simple(private val contents: Either<ResourceKey<Block>, Block>) : Delegated<Block, Item> {
        @Suppress("DEPRECATION")
        override fun getBlockHolder(): Holder<Block> = contents.map(BuiltInRegistries.BLOCK::getHolderOrThrow, Block::builtInRegistryHolder)

        override fun asBlock(): Block = getBlockHolder().value()

        @Suppress("DEPRECATION")
        override fun getItemHolder(): Holder<Item> = asItem().builtInRegistryHolder()

        override fun asItem(): Item = asBlock().asItem()
    }
}
