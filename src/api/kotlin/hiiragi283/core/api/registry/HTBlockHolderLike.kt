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
            .xmap(::Simple, HTBlockHolderLike<*, *>::getBlockKey)

        @JvmField
        val HOLDER_CODEC: BiCodec<RegistryFriendlyByteBuf, HTBlockHolderLike<*, *>> = VanillaBiCodecs
            .holder(Registries.BLOCK)
            .xmap(Holder<Block>::value.andThen(::Simple), HTBlockHolderLike<*, *>::getBlockHolder)
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
     * @since 0.8.0
     */
    class Simple(private val contents: Either<ResourceKey<Block>, Block>) : Delegated<Block, Item> {
        constructor(key: ResourceKey<Block>) : this(Either.Left(key))

        constructor(id: ResourceLocation) : this(Registries.BLOCK.createKey(id))

        constructor(block: Block) : this(Either.Right(block))

        @Suppress("DEPRECATION")
        override fun getBlockHolder(): Holder<Block> = contents.map(BuiltInRegistries.BLOCK::getHolderOrThrow, Block::builtInRegistryHolder)

        override fun asBlock(): Block = getBlockHolder().value()

        @Suppress("DEPRECATION")
        override fun getItemHolder(): Holder<Item> = asItem().builtInRegistryHolder()

        override fun asItem(): Item = asBlock().asItem()
    }
}
