package hiiragi283.core.api.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartKey
import hiiragi283.core.api.material.part.property.tagPrefix
import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.toFraction
import hiiragi283.core.api.util.DFUEither
import hiiragi283.core.api.util.HTTextResult
import hiiragi283.core.api.util.getOrElse
import hiiragi283.core.api.util.right
import hiiragi283.core.api.util.toTextResult
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
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
@JvmRecord
data class HTItemResult(val entry: Entry, val count: Int) : HTIdLike {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTItemResult> = HTCodecs.recordMap { instance ->
            instance.group(
                Entry.MAP_CODEC.forGetter(HTItemResult::entry),
                HTCodecs.POSITIVE_INT.fieldOf(HTConst.COUNT).orElse(1).forGetter(HTItemResult::count),
            ).apply(instance, ::HTItemResult)
        }

        @JvmField
        val CODEC: Codec<HTItemResult> = Codec.withAlternative(MAP_CODEC.codec(), Entry.MAP_CODEC.codec()) { it.toResult() }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> = StreamCodec.composite(
            Entry.STREAM_CODEC,
            HTItemResult::entry,
            ByteBufCodecs.VAR_INT,
            HTItemResult::count,
            ::HTItemResult,
        )
    }

    constructor(stack: ItemStack) : this(SimpleEntry(stack), stack.count)

    /**
     * アイテムの完成品を作成します。
     */
    fun create(): HTTextResult<ItemStack> = entry.create().map { it.copyWithCount(count) }

    /**
     * アイテムの完成品を作成します。
     * @return 正常に作成できなかった場合は[ItemStack.EMPTY]
     */
    fun createOrEmpty(): ItemStack = create().getOrElse { ItemStack.EMPTY }

    fun isIncomplete(): Boolean = create().isLeft()

    /**
     * このインスタンスのコピーを作成します。
     * @param newCount 新しい個数
     */
    fun copyWithCount(newCount: Int): HTItemResult = HTItemResult(entry, newCount)

    /**
     * 確率付きの完成品に変換します。
     */
    infix fun withChance(chance: Float = 1f): HTChancedItemResult = withChance(chance.toFraction())

    /**
     * 確率付きの完成品に変換します。
     */
    infix fun withChance(chance: Fraction): HTChancedItemResult = HTChancedItemResult(this, chance)

    override fun getId(): ResourceLocation = entry.getId()

    //    Entry    //

    interface Entry : HTIdLike {
        companion object {
            @JvmField
            val MAP_CODEC: MapCodec<Entry> = NeoForgeExtraCodecs.dispatchMapOrElse(
                HCRegistries.ITEM_RESULT_TYPE.byNameCodec(),
                Entry::type,
                HTItemResultType<*>::codec,
                SimpleEntry.MAP_CODEC,
            ).xmap(
                { DFUEither.unwrap(it) },
                { entry: Entry ->
                    when (entry) {
                        is SimpleEntry -> DFUEither.right(entry)
                        else -> DFUEither.left(entry)
                    }
                },
            )

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Entry> = ByteBufCodecs.registry(HCRegistries.Keys.ITEM_RESULT_TYPE).dispatch(Entry::type, HTItemResultType<*>::streamCodec)
        }

        fun type(): HTItemResultType<*>

        fun create(): HTTextResult<ItemStack>

        fun toResult(count: Int = 1): HTItemResult = HTItemResult(this, count)
    }

    @JvmRecord
    data class SimpleEntry @JvmOverloads constructor(val item: Holder<Item>, val components: DataComponentPatch = DataComponentPatch.EMPTY) :
        Entry,
        HTKeyLike<Item> {
        companion object {
            @JvmField
            val MAP_CODEC: MapCodec<SimpleEntry> = HTCodecs.recordMap { instance ->
                instance.group(
                    ItemStack.ITEM_NON_AIR_CODEC.fieldOf(HTConst.ID).forGetter(SimpleEntry::item),
                    DataComponentPatch.CODEC.optionalFieldOf(HTConst.COMPONENTS, DataComponentPatch.EMPTY).forGetter(SimpleEntry::components),
                ).apply(instance, ::SimpleEntry)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SimpleEntry> = StreamCodec.composite(
                HTStreamCodecs.holder(Registries.ITEM),
                SimpleEntry::item,
                DataComponentPatch.STREAM_CODEC,
                SimpleEntry::components,
                ::SimpleEntry,
            )

            @JvmField
            val TYPE: HTItemResultType<SimpleEntry> = HTItemResultType(MAP_CODEC, STREAM_CODEC)
        }

        constructor(stack: ItemStack) : this(stack.itemHolder, stack.componentsPatch)

        override fun type(): HTItemResultType<*> = TYPE

        override fun create(): HTTextResult<ItemStack> = ItemStack(item, 1, components).right()

        override fun getKey(): ResourceKey<Item> = item.getKeyOrThrow()
    }

    @JvmInline
    value class TagEntry(val tagKey: TagKey<Item>) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<TagEntry> = HTCodecs.tagKey(Registries.ITEM, true).fieldOf(HTConst.TAG).xmap(::TagEntry, TagEntry::tagKey)

            @JvmField
            val STREAM_CODEC: StreamCodec<ByteBuf, TagEntry> = HTStreamCodecs.tagKey(Registries.ITEM).map(::TagEntry, TagEntry::tagKey)

            @JvmField
            val TYPE: HTItemResultType<TagEntry> = HTItemResultType(CODEC, STREAM_CODEC.cast())
        }

        override fun type(): HTItemResultType<*> = TYPE

        override fun create(): HTTextResult<ItemStack> = HiiragiCoreAccess.INSTANCE.getFirstHolder(BuiltInRegistries.ITEM.asLookup(), tagKey).map { ItemStack(it.get()) }

        override fun getId(): ResourceLocation = tagKey.location()
    }

    @JvmRecord
    data class MaterialPartEntry(val part: HTPart, val key: HTMaterialKey) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<MaterialPartEntry> = HTCodecs.recordMap { instance ->
                instance
                    .group(
                        HTPart.CODEC.fieldOf("part").forGetter(MaterialPartEntry::part),
                        HTMaterialKey.CODEC.fieldOf("material").forGetter(MaterialPartEntry::key),
                    ).apply(instance, ::MaterialPartEntry)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<ByteBuf, MaterialPartEntry> = StreamCodec.composite(
                HTPart.STREAM_CODEC,
                MaterialPartEntry::part,
                HTMaterialKey.STREAM_CODEC,
                MaterialPartEntry::key,
                ::MaterialPartEntry,
            )

            @JvmField
            val TYPE: HTItemResultType<MaterialPartEntry> = HTItemResultType(CODEC, STREAM_CODEC.cast())
        }

        constructor(part: HTPartKey, key: HTMaterialKey) : this(HTPart.getManager().getOrThrow(part), key)

        override fun type(): HTItemResultType<*> = TYPE

        override fun create(): HTTextResult<ItemStack> {
            val tagResult: HTTextResult<ItemStack>? = part.tagPrefix
                ?.itemTagKey(key)
                ?.let(::TagEntry)
                ?.create()
            if (tagResult != null && tagResult.isLeft()) {
                return tagResult
            }
            return HiiragiCoreAccess.INSTANCE
                .getMaterialBlockOrItem(part.key, key)
                .toTextResult { "No matching item for part ${part.key} and material $key" }
                .map { it.toStack() }
        }

        override fun getId(): ResourceLocation = part.createId(key)
    }
}
