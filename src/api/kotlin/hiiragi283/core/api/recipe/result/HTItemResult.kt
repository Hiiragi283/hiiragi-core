package hiiragi283.core.api.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.HTPartManager
import hiiragi283.core.api.material.part.tagPrefix
import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.toFraction
import hiiragi283.core.api.util.DFUEither
import hiiragi283.core.api.util.HTTextResult
import hiiragi283.core.api.util.getOrElse
import hiiragi283.core.api.util.right
import hiiragi283.core.api.util.toTextResult
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import org.apache.commons.lang3.math.Fraction

/**
 * アイテムの完成品を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
interface HTItemResult : HTIdLike {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTItemResult> = NeoForgeExtraCodecs.dispatchMapOrElse(
            HCRegistries.ITEM_RESULT_SERIALIZER.byNameCodec(),
            HTItemResult::getSerializer,
            Serializer<*>::codec,
            Simple.MAP_CODEC,
        ).xmap(DFUEither<HTItemResult, Simple>::unwrap) { result: HTItemResult ->
            when (result) {
                is Simple -> DFUEither.right(result)
                else -> DFUEither.left(result)
            }
        }

        @JvmField
        val CODEC: Codec<HTItemResult> = Codec.lazyInitialized(MAP_CODEC::codec)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> = ByteBufCodecs
            .registry(HCRegistries.Keys.ITEM_RESULT_SERIALIZER)
            .dispatch(HTItemResult::getSerializer, Serializer<*>::streamCodec)
    }

    fun getSerializer(): Serializer<*>

    /**
     * アイテムの完成品を作成します。
     */
    fun create(): HTTextResult<ItemStack>

    /**
     * アイテムの完成品を作成します。
     * @return 正常に作成できなかった場合は[ItemStack.EMPTY]
     */
    fun createOrEmpty(): ItemStack = create().getOrElse { ItemStack.EMPTY }

    fun isIncomplete(): Boolean = create().isLeft()

    /**
     * 完成品の個数
     */
    val count: Int

    /**
     * このインスタンスのコピーを作成します。
     * @param newCount 新しい個数
     */
    fun copyWithCount(newCount: Int): HTItemResult

    /**
     * 確率付きの完成品に変換します。
     */
    infix fun withChance(chance: Float = 1f): HTChancedItemResult = withChance(chance.toFraction())

    /**
     * 確率付きの完成品に変換します。
     */
    infix fun withChance(chance: Fraction): HTChancedItemResult = HTChancedItemResult(this, chance)

    //    Serializer    //

    @JvmRecord
    data class Serializer<T : HTItemResult>(val codec: MapCodec<T>, val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>) {
        constructor(codec: MapCodec<T>) : this(codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()))
    }

    //    Simple    //

    @JvmInline
    value class Simple(private val template: ItemStack) :
        HTItemResult,
        HTKeyLike<Item> {
        companion object {
            @JvmField
            val MAP_CODEC: MapCodec<Simple> = ItemStack.CODEC.xmap(::Simple, Simple::template).let { MapCodec.assumeMapUnsafe(it) }

            @JvmField
            val CODEC: Codec<Simple> = Codec.lazyInitialized(MAP_CODEC::codec)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Simple> = ItemStack.STREAM_CODEC.map(::Simple, Simple::template)

            @JvmField
            val SERIALIZER: Serializer<Simple> = Serializer(MAP_CODEC, STREAM_CODEC)
        }

        override val count: Int get() = template.count

        override fun getSerializer(): Serializer<*> = SERIALIZER

        override fun create(): HTTextResult<ItemStack> = template.copy().right()

        override fun copyWithCount(newCount: Int): Simple = Simple(template.copyWithCount(newCount))

        override fun getKey(): ResourceKey<Item> = template.itemHolder.getKeyOrThrow()
    }

    //    Tagged    //

    data class Tagged(val tagKey: TagKey<Item>, override val count: Int = 1) : HTItemResult {
        companion object {
            @JvmField
            val CODEC: MapCodec<Tagged> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTCodecs.tagKey(Registries.ITEM, true).fieldOf(HTConst.TAG).forGetter(Tagged::tagKey),
                    HTCodecs.POSITIVE_INT.optionalFieldOf(HTConst.COUNT, 1).forGetter(Tagged::count),
                ).apply(instance, ::Tagged)
            }

            @JvmField
            val SERIALIZER: Serializer<Tagged> = Serializer(CODEC)
        }

        override fun getSerializer(): Serializer<*> = SERIALIZER

        override fun create(): HTTextResult<ItemStack> = HiiragiCoreAccess.INSTANCE.getFirstHolder(BuiltInRegistries.ITEM.asLookup(), tagKey).map { ItemStack(it.get(), count) }

        override fun copyWithCount(newCount: Int): Tagged = this.copy(count = newCount)

        override fun getId(): ResourceLocation = tagKey.location()
    }

    //    MaterialPart    //

    @JvmRecord
    data class MaterialPart(val part: HTPart, val key: HTMaterialKey, override val count: Int = 1) : HTItemResult {
        companion object {
            @JvmField
            val CODEC: MapCodec<MaterialPart> = HTCodecs.recordMap { instance ->
                instance
                    .group(
                        HTPartManager.CODEC
                            .fieldOf("part")
                            .forGetter(MaterialPart::part),
                        HTMaterialKey.CODEC.fieldOf("material").forGetter(MaterialPart::key),
                        HTCodecs.POSITIVE_INT
                            .fieldOf(HTConst.COUNT)
                            .orElse(1)
                            .forGetter(MaterialPart::count),
                    ).apply(instance, ::MaterialPart)
            }

            @JvmField
            val SERIALIZER: Serializer<MaterialPart> = Serializer(CODEC)
        }

        constructor(part: HTPartLike, key: HTMaterialKey, count: Int = 1) : this(part.asPart(), key, count)

        override fun getSerializer(): Serializer<*> = SERIALIZER

        override fun create(): HTTextResult<ItemStack> {
            val tagResult: HTTextResult<ItemStack>? = part.tagPrefix
                ?.itemTagKey(key)
                ?.let { Tagged(it, count) }
                ?.create()
            if (tagResult != null && tagResult.isLeft()) {
                return tagResult
            }
            return HiiragiCoreAccess.INSTANCE
                .getMaterialBlockOrItem(part, key)
                .toTextResult { "No matching item for part ${part.key} and material $key" }
                .map { it.toStack(count) }
        }

        override fun copyWithCount(newCount: Int): MaterialPart = this.copy(count = newCount)

        override fun getId(): ResourceLocation = part.createId(key)
    }
}
