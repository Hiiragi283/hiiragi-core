package hiiragi283.lib.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTPlatform
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.math.toFraction
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.registry.getResult
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.convert
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.flatMap
import hiiragi283.lib.util.getOrElse
import hiiragi283.lib.util.right
import hiiragi283.lib.util.toTextResult
import hiiragi283.lib.util.unwrap
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import org.apache.commons.lang3.math.Fraction

/**
 * アイテムの完成品を提供するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemResult : HTIdLike {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTItemResult> = NeoForgeExtraCodecs.dispatchMapOrElse(
            HTRegistries.ITEM_RESULT_SERIALIZER.byNameCodec(),
            HTItemResult::getSerializer,
            Serializer<*>::codec,
            Simple.MAP_CODEC,
        ).convert().xmap(
            { it.unwrap() },
            { result: HTItemResult ->
                when (result) {
                    is Simple -> Either.Right(result)
                    else -> Either.Left(result)
                }
            },
        )

        @JvmField
        val CODEC: Codec<HTItemResult> = Codec.lazyInitialized(MAP_CODEC::codec)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> = ByteBufCodecs.registry(HTRegistries.Keys.ITEM_RESULT_SERIALIZER).dispatch(HTItemResult::getSerializer, Serializer<*>::streamCodec)
    }

    /**
     * シリアライザを取得します。
     */
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

    /**
     * [HTItemResult]のシリアライザとなるクラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmRecord
    data class Serializer<T : HTItemResult>(val codec: MapCodec<T>, val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>) {
        constructor(codec: MapCodec<T>) : this(codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()))
    }

    //    Simple    //

    /**
     * [ItemStackTemplate]に基づいだ[HTItemResult]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmInline
    value class Simple(private val template: ItemStackTemplate) : HTItemResult {
        companion object {
            @JvmField
            val MAP_CODEC: MapCodec<Simple> = ItemStackTemplate.MAP_CODEC.xmap(::Simple, Simple::template)

            @JvmField
            val CODEC: Codec<Simple> = Codec.lazyInitialized(MAP_CODEC::codec)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Simple> = ItemStackTemplate.STREAM_CODEC.map(::Simple, Simple::template)

            @JvmField
            val SERIALIZER: Serializer<Simple> = Serializer(MAP_CODEC, STREAM_CODEC)
        }

        override val count: Int get() = template.count

        override fun getSerializer(): Serializer<*> = SERIALIZER

        override fun create(): HTTextResult<ItemStack> = template.create().right()

        override fun copyWithCount(newCount: Int): Simple = Simple(template.withCount(newCount))

        override fun getId(): Identifier = template.typeHolder().getKeyOrThrow().identifier()
    }

    //    Tagged    //

    /**
     * [TagKey]に基づいだ[HTItemResult]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmRecord
    data class Tagged(val tagKey: TagKey<Item>, override val count: Int = 1) : HTItemResult {
        companion object {
            @JvmField
            val CODEC: MapCodec<Tagged> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTCodecs.tagKey(Registries.ITEM, true).fieldOf(HTConstants.TAG).forGetter(Tagged::tagKey),
                    HTCodecs.POSITIVE_INT.optionalFieldOf(HTConstants.COUNT, 1).forGetter(Tagged::count),
                ).apply(instance, ::Tagged)
            }

            @JvmField
            val SERIALIZER: Serializer<Tagged> = Serializer(CODEC)
        }

        override fun getSerializer(): Serializer<*> = SERIALIZER

        override fun create(): HTTextResult<ItemStack> = HTPlatform.INSTANCE.getFirstHolder(tagKey).map { ItemStack(it.get(), count) }

        override fun copyWithCount(newCount: Int): Tagged = this.copy(count = newCount)

        override fun getId(): Identifier = tagKey.location()
    }

    //    MaterialPart    //

    /**
     * 部品と素材に基づいだ[HTItemResult]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmRecord
    data class MaterialPart(val part: HTMaterialPartKey, val material: HTMaterialKey, override val count: Int = 1) : HTItemResult {
        companion object {
            @JvmField
            val CODEC: MapCodec<MaterialPart> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTMaterialPartKey.CODEC.fieldOf("part").forGetter(MaterialPart::part),
                    HTCodecs.resourceKey(HTRegistries.Keys.MATERIAL_CONTENTS).fieldOf(HTConstants.MATERIAL).forGetter(MaterialPart::material),
                    HTCodecs.POSITIVE_INT.optionalFieldOf(HTConstants.COUNT, 1).forGetter(MaterialPart::count),
                ).apply(instance, ::MaterialPart)
            }

            @JvmField
            val SERIALIZER: Serializer<MaterialPart> = Serializer(CODEC)
        }

        override fun getSerializer(): Serializer<*> = SERIALIZER

        override fun create(): HTTextResult<ItemStack> = HTRegistries.MATERIAL_CONTENTS
            .getResult(material)
            .map { it.value() }
            .flatMap { it.getEntry(part).toTextResult { "Unknown item for part $part and material $material" } }
            .map { it.toStack() }

        override fun copyWithCount(newCount: Int): MaterialPart = this.copy(count = newCount)

        override fun getId(): Identifier = material.identifier().withPath { "${part.name}/$it" }
    }
}
