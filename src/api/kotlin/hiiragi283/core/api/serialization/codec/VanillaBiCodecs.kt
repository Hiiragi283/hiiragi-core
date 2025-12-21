package hiiragi283.core.api.serialization.codec

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.tag.createTagKey
import io.netty.buffer.ByteBuf
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.UUIDUtil
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.UUID

/**
 * バニラのクラスに関連する[BiCodec]や[VanillaBiCodecs]を集めたクラスです。
 */
object VanillaBiCodecs {
    /**
     * [ResourceLocation]の[BiCodec]
     */
    @JvmField
    val ID: BiCodec<ByteBuf, ResourceLocation> = BiCodec.of(ResourceLocation.CODEC, ResourceLocation.STREAM_CODEC)

    /**
     * [DyeColor]の[BiCodec]
     */
    @JvmField
    val COLOR: BiCodec<ByteBuf, DyeColor> = BiCodec.of(DyeColor.CODEC, DyeColor.STREAM_CODEC)

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
     * [InteractionHand]の[BiCodec]
     */
    @JvmField
    val HAND: BiCodec<ByteBuf, InteractionHand> = BiCodecs.enum()

    /**
     * [PotionContents]の[BiCodec]
     */
    @JvmField
    val POTION: BiCodec<RegistryFriendlyByteBuf, PotionContents> =
        BiCodec.of(PotionContents.CODEC, PotionContents.STREAM_CODEC)

    /**
     * [Component]の[BiCodec]
     */
    @JvmField
    val TEXT: BiCodec<RegistryFriendlyByteBuf, Component> =
        BiCodec.of(ComponentSerialization.CODEC, ComponentSerialization.STREAM_CODEC)

    /**
     * [UUID]の[BiCodec]
     */
    @JvmField
    val UUID: BiCodec<ByteBuf, UUID> = BiCodec.of(UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC)

    @JvmField
    val FLUID_STACK_NON_EMPTY: BiCodec<RegistryFriendlyByteBuf, FluidStack> = BiCodec.of(FluidStack.CODEC, FluidStack.STREAM_CODEC)

    @JvmField
    val FLUID_STACK: BiCodec<RegistryFriendlyByteBuf, FluidStack> = BiCodec.of(FluidStack.OPTIONAL_CODEC, FluidStack.OPTIONAL_STREAM_CODEC)

    /**
     * [Ingredient]の[BiCodec]
     */
    @JvmField
    val INGREDIENT: BiCodec<RegistryFriendlyByteBuf, Ingredient> = BiCodec.of(HTIngredientCodec.ITEM, Ingredient.CONTENTS_STREAM_CODEC)

    /**
     * [FluidIngredient]の[MapBiCodec]
     */
    @JvmField
    val FLUID_INGREDIENT: MapBiCodec<RegistryFriendlyByteBuf, FluidIngredient> = MapBiCodec.of(
        HTIngredientCodec.FLUID,
        FluidIngredient.STREAM_CODEC,
    )

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
        ResourceLocation.STREAM_CODEC.map(registryKey::createTagKey, TagKey<T>::location),
    )

    /**
     * 指定した[registryKey]から[Holder]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holder(registryKey: RegistryKey<T>): BiCodec<RegistryFriendlyByteBuf, Holder<T>> =
        BiCodec.of(RegistryFixedCodec.create(registryKey), ByteBufCodecs.holderRegistry(registryKey))

    /**
     * 指定した[registryKey]から[HolderSet]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holderSet(registryKey: RegistryKey<T>): BiCodec<RegistryFriendlyByteBuf, HolderSet<T>> =
        BiCodec.of(RegistryCodecs.homogeneousList(registryKey), ByteBufCodecs.holderSet(registryKey))
}
