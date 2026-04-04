package hiiragi283.core.api.serialization.codec

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.serialization.codec.impl.HTHolderLikeCodec
import hiiragi283.core.api.serialization.codec.impl.HTHolderLikeStreamCodec
import hiiragi283.core.api.tag.createTagKey
import hiiragi283.core.api.text.Text
import io.netty.buffer.ByteBuf
import net.minecraft.advancements.criterion.MinMaxBounds
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.GlobalPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.UUIDUtil
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.Identifier
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.UUID

/**
 * バニラのクラスに関連する[BiCodec]や[VanillaBiCodecs]を集めたクラスです。
 */
object VanillaBiCodecs {
    /**
     * [Identifier]の[BiCodec]
     */
    @JvmField
    val ID: BiCodec<ByteBuf, Identifier> = BiCodec.of(Identifier.CODEC, Identifier.STREAM_CODEC)

    /**
     * [BlockPos]の[BiCodec]
     * @since 0.5.0
     */
    @JvmField
    val BLOCK_POS: BiCodec<ByteBuf, BlockPos> = BiCodec.of(BlockPos.CODEC, BlockPos.STREAM_CODEC)

    /**
     * [DataComponentPatch]の[MapBiCodec]
     */
    @JvmField
    val COMPONENT_PATCH: MapBiCodec<RegistryFriendlyByteBuf, DataComponentPatch> =
        BiCodec
            .of(DataComponentPatch.CODEC, DataComponentPatch.STREAM_CODEC)
            .optionalFieldOf(HTConst.COMPONENTS, DataComponentPatch.EMPTY)

    /**
     * [Direction]の[BiCodec]
     */
    @JvmField
    val DIRECTION: BiCodec<ByteBuf, Direction> = BiCodec.of(Direction.CODEC, Direction.STREAM_CODEC)

    /**
     * [GlobalPos]の[BiCodec]
     * @since 0.5.0
     */
    @JvmField
    val GLOBAL_POS: BiCodec<ByteBuf, GlobalPos> = BiCodec.of(GlobalPos.CODEC, GlobalPos.STREAM_CODEC)

    /**
     * [PotionContents]の[BiCodec]
     */
    @JvmField
    val POTION: BiCodec<RegistryFriendlyByteBuf, PotionContents> =
        BiCodec.of(PotionContents.CODEC, PotionContents.STREAM_CODEC)

    /**
     * [Text]の[BiCodec]
     */
    @JvmField
    val TEXT: BiCodec<RegistryFriendlyByteBuf, Text> =
        BiCodec.of(ComponentSerialization.CODEC, ComponentSerialization.STREAM_CODEC)

    /**
     * [UUID]の[BiCodec]
     */
    @JvmField
    val UUID: BiCodec<ByteBuf, UUID> = BiCodec.of(UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC)

    // Bounds
    @JvmField
    val INT_BOUNDS: BiCodec<ByteBuf, MinMaxBounds.Ints> =
        BiCodec.of(MinMaxBounds.Ints.CODEC, MinMaxBounds.Ints.STREAM_CODEC)

    @JvmField
    val FLOAT_BOUNDS: BiCodec<ByteBuf, MinMaxBounds.FloatDegrees> =
        BiCodec.of(MinMaxBounds.FloatDegrees.CODEC, MinMaxBounds.FloatDegrees.STREAM_CODEC)

    @JvmField
    val DOUBLE_BOUNDS: BiCodec<ByteBuf, MinMaxBounds.Doubles> =
        BiCodec.of(MinMaxBounds.Doubles.CODEC, MinMaxBounds.Doubles.STREAM_CODEC)

    // Typed Instances
    /**
     * [ItemStackTemplate]の[BiCodec]
     */
    @JvmField
    val ITEM_STACK_TEMPLATE: BiCodec<RegistryFriendlyByteBuf, ItemStackTemplate> =
        BiCodec.of(ItemStackTemplate.CODEC, ItemStackTemplate.STREAM_CODEC)

    /**
     * [FluidStackTemplate]の[BiCodec]
     */
    @JvmField
    val FLUID_STACK_TEMPLATE: BiCodec<RegistryFriendlyByteBuf, FluidStackTemplate> =
        BiCodec.of(FluidStackTemplate.CODEC, FluidStackTemplate.STREAM_CODEC)

    /**
     * [ItemStack]の[BiCodec]
     */
    @JvmStatic
    fun itemStack(alloyEmpty: Boolean): BiCodec<RegistryFriendlyByteBuf, ItemStack> = when (alloyEmpty) {
        true -> BiCodec.of(ItemStack.OPTIONAL_CODEC, ItemStack.OPTIONAL_STREAM_CODEC)
        false -> BiCodec.of(ItemStack.CODEC, ItemStack.STREAM_CODEC)
    }

    /**
     * [FluidStack]の[BiCodec]
     */
    @JvmStatic
    fun fluidStack(alloyEmpty: Boolean): BiCodec<RegistryFriendlyByteBuf, FluidStack> = when (alloyEmpty) {
        true -> BiCodec.of(FluidStack.OPTIONAL_CODEC, FluidStack.OPTIONAL_STREAM_CODEC)
        false -> BiCodec.of(FluidStack.CODEC, FluidStack.STREAM_CODEC)
    }

    // Transfer

    /**
     * [ItemResource]の[BiCodec]
     */
    @JvmStatic
    fun itemResource(alloyEmpty: Boolean): BiCodec<RegistryFriendlyByteBuf, ItemResource> = when (alloyEmpty) {
        true -> BiCodec.of(ItemResource.OPTIONAL_CODEC, ItemResource.STREAM_CODEC)
        false -> BiCodec.of(ItemResource.CODEC, ItemResource.STREAM_CODEC)
    }

    /**
     * [FluidResource]の[BiCodec]
     */
    @JvmStatic
    fun fluidResource(alloyEmpty: Boolean): BiCodec<RegistryFriendlyByteBuf, FluidResource> = when (alloyEmpty) {
        true -> BiCodec.of(FluidResource.OPTIONAL_CODEC, FluidResource.STREAM_CODEC)
        false -> BiCodec.of(FluidResource.CODEC, FluidResource.STREAM_CODEC)
    }

    // Recipe

    /**
     * [Ingredient]の[BiCodec]
     */
    @JvmField
    val INGREDIENT: BiCodec<RegistryFriendlyByteBuf, Ingredient> =
        BiCodec.of(Ingredient.CODEC, Ingredient.CONTENTS_STREAM_CODEC)

    /**
     * [FluidIngredient]の[BiCodec]
     */
    @JvmField
    val FLUID_INGREDIENT: BiCodec<RegistryFriendlyByteBuf, FluidIngredient> =
        BiCodec.of(FluidIngredient.CODEC, FluidIngredient.STREAM_CODEC)

    /**
     * [SizedIngredient]の[BiCodec]
     */
    @JvmField
    val SIZED_INGREDIENT: BiCodec<RegistryFriendlyByteBuf, SizedIngredient> =
        BiCodec.of(SizedIngredient.NESTED_CODEC, SizedIngredient.STREAM_CODEC)

    /**
     * [SizedFluidIngredient]の[BiCodec]
     */
    @JvmField
    val SIZED_FLUID_INGREDIENT: BiCodec<RegistryFriendlyByteBuf, SizedFluidIngredient> =
        BiCodec.of(SizedFluidIngredient.CODEC, SizedFluidIngredient.STREAM_CODEC)

    // Registry

    /**
     * 指定した[registryKey]から[ResourceKey]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> resourceKey(registryKey: RegistryKey<T>): BiCodec<ByteBuf, ResourceKey<T>> =
        BiCodec.of(ResourceKey.codec(registryKey), ResourceKey.streamCodec(registryKey))

    /**
     * 指定した[registryKey]から[TagKey]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     * @param withHash 変換後の文字列の先頭に'#'をつけるかどうか
     */
    @JvmStatic
    fun <T : Any> tagKey(registryKey: RegistryKey<T>, withHash: Boolean): BiCodec<ByteBuf, TagKey<T>> = BiCodec.of(
        when (withHash) {
            true -> TagKey.hashedCodec(registryKey)
            false -> TagKey.codec(registryKey)
        },
        Identifier.STREAM_CODEC.map(registryKey::createTagKey, TagKey<T>::location),
    )

    @JvmStatic
    fun <T : Any> registryBased(registry: Registry<T>): BiCodec<RegistryFriendlyByteBuf, T> =
        BiCodec.of(registry.byNameCodec(), ByteBufCodecs.registry(registry.key()))

    /**
     * 指定した[registryKey]から[Holder]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holder(registryKey: RegistryKey<T>): BiCodec<RegistryFriendlyByteBuf, Holder<T>> = BiCodec
        .of(RegistryFixedCodec.create(registryKey), ByteBufCodecs.holderRegistry(registryKey))
        .filterOrElse({ holder: Holder<T> -> holder.unwrap().left().isPresent }, Holder<T>::getDelegate)

    /**
     * 指定した[registryKey]から[HolderSet]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holderSet(registryKey: RegistryKey<T>): BiCodec<RegistryFriendlyByteBuf, HolderSet<T>> =
        BiCodec.of(RegistryCodecs.homogeneousList(registryKey), ByteBufCodecs.holderSet(registryKey))

    /**
     * 指定した[registryKey]から[HTSimpleHolderLike]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     * @since 0.13.0
     */
    @JvmStatic
    fun <T : Any> holderLike(registryKey: RegistryKey<T>): BiCodec<RegistryFriendlyByteBuf, HTSimpleHolderLike<T>> =
        BiCodec.of(HTHolderLikeCodec(registryKey), HTHolderLikeStreamCodec(registryKey))
}
