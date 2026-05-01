package hiiragi283.core.api.recipe.result

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.compareTo
import hiiragi283.core.api.item.createEnchantedBook
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.tagPrefix
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.serialization.network.toOptional
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.storage.item.toStackOrEmpty
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.util.wrapOptional
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import org.apache.commons.lang3.math.Fraction
import java.util.Optional

@JvmRecord
data class HTItemResult(val entry: Entry, val chance: Fraction, val fallback: Optional<Entry>) : HTRecipeResult<ItemStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemResult> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Entry.MAP_CODEC.forGetter(HTItemResult::entry),
                    HTCodecs.FRACTION
                        .validate(Codec.checkRange(Fraction.ZERO, Fraction.ONE))
                        .optionalFieldOf(HTConst.CHANCE, Fraction.ONE)
                        .forGetter(HTItemResult::chance),
                    Entry.CODEC.optionalFieldOf("fallback").forGetter(HTItemResult::fallback),
                ).apply(instance, ::HTItemResult)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> = StreamCodec.composite(
            Entry.STREAM_CODEC,
            HTItemResult::entry,
            HTStreamCodecs.FRACTION,
            HTItemResult::chance,
            Entry.STREAM_CODEC.toOptional(),
            HTItemResult::fallback,
            ::HTItemResult,
        )
    }

    constructor(entry: Entry, chance: Fraction, fallback: Entry? = null) : this(entry, chance, fallback.wrapOptional())

    constructor(stack: ItemStack, chance: Fraction) : this(SimpleEntry(stack), chance)

    constructor(stack: ItemStack) : this(stack, Fraction.ONE)

    override fun get(): HTTextResult<ItemStack> = get(false)

    fun get(preview: Boolean): HTTextResult<ItemStack> = when {
        !preview && HiiragiCoreAPI.RANDOM.nextFloat() >= chance -> HTTextResult.success(ItemStack.EMPTY)
        else -> entry.get()
    }

    fun getOrEmpty(): ItemStack = get().valueOrElse(ItemStack::EMPTY)

    override fun getId(): ResourceLocation = entry.getId()

    //    Type    //

    interface Entry : SupplierWithId<HTTextResult<ItemStack>> {
        companion object {
            @JvmField
            val MAP_CODEC: MapCodec<Entry> = NeoForgeExtraCodecs
                .dispatchMapOrElse(
                    HCRegistries.ITEM_RESULT_TYPE.byNameCodec(),
                    Entry::type,
                    EntryType<*>::codec,
                    SimpleEntry.CODEC,
                ).xmap(
                    { either: Either<Entry, SimpleEntry> -> Either.unwrap(either) },
                    { entry: Entry ->
                        when (entry) {
                            is SimpleEntry -> Either.right(entry)
                            else -> Either.left(entry)
                        }
                    },
                )

            @JvmField
            val CODEC: Codec<Entry> = MAP_CODEC.codec()

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Entry> = ByteBufCodecs
                .registry(HCRegistries.Keys.ITEM_RESULT_TYPE)
                .dispatch(Entry::type, EntryType<*>::streamCodec)
        }

        fun type(): EntryType<*>
    }

    //    EntryType    //

    @JvmRecord
    data class EntryType<T : Entry>(
        val codec: MapCodec<T>,
        val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T> = ByteBufCodecs.fromCodecWithRegistries(codec.codec()),
    )

    //    SimpleEntry    //

    @JvmRecord
    data class SimpleEntry(val stack: ItemStack) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<SimpleEntry> =
                ItemStack.CODEC.xmap(::SimpleEntry, SimpleEntry::stack).let { MapCodec.assumeMapUnsafe(it) }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SimpleEntry> =
                ItemStack.STREAM_CODEC.map(::SimpleEntry, SimpleEntry::stack)

            @JvmField
            val TYPE: EntryType<SimpleEntry> = EntryType(CODEC, STREAM_CODEC)
        }

        override fun type(): EntryType<*> = TYPE

        override fun get(): HTTextResult<ItemStack> = HTTextResult.success(stack)

        override fun getId(): ResourceLocation = stack.itemHolder.toLike().getId()
    }

    //    TagEntry    //

    @JvmRecord
    data class TagEntry(val items: HolderSet<Item>, val count: Int) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<TagEntry> = RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        HTCodecs.holderSet(Registries.ITEM).fieldOf(HTConst.ITEMS).forGetter(TagEntry::items),
                        HTCodecs.POSITIVE_INT
                            .fieldOf(HTConst.COUNT)
                            .orElse(1)
                            .forGetter(TagEntry::count),
                    ).apply(instance, ::TagEntry)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TagEntry> = StreamCodec.composite(
                HTStreamCodecs.holderSet(Registries.ITEM),
                TagEntry::items,
                ByteBufCodecs.VAR_INT,
                TagEntry::count,
                ::TagEntry,
            )

            @JvmField
            val TYPE: EntryType<TagEntry> = EntryType(CODEC, STREAM_CODEC)
        }

        override fun type(): EntryType<*> = TYPE

        override fun get(): HTTextResult<ItemStack> = HiiragiCoreAccess.INSTANCE.getFirstHolder(items).map { ItemStack(it.get(), count) }

        override fun getId(): ResourceLocation = items.unwrapKey().orElseThrow().location()
    }

    //    EnchantedBookEntry    //

    @JvmRecord
    data class EnchantedBookEntry(val holder: Holder<Enchantment>) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<EnchantedBookEntry> = HTCodecs
                .holder(Registries.ENCHANTMENT)
                .fieldOf("enchantment")
                .xmap(::EnchantedBookEntry, EnchantedBookEntry::holder)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EnchantedBookEntry> =
                HTStreamCodecs.holder(Registries.ENCHANTMENT).map(::EnchantedBookEntry, EnchantedBookEntry::holder)

            @JvmField
            val TYPE: EntryType<EnchantedBookEntry> = EntryType(CODEC, STREAM_CODEC)
        }

        override fun type(): EntryType<*> = TYPE

        override fun get(): HTTextResult<ItemStack> = HTTextResult.success(createEnchantedBook(holder))

        override fun getId(): ResourceLocation = holder.toLike().getId()
    }

    //    MaterialPartEntry    //

    @JvmRecord
    data class MaterialPartEntry(val part: HTPart, val material: HTMaterialKey, val count: Int) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<MaterialPartEntry> = RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        HiiragiCoreAccess.INSTANCE.partCodec
                            .fieldOf("part")
                            .forGetter(MaterialPartEntry::part),
                        HTMaterialKey.CODEC.fieldOf("material").forGetter(MaterialPartEntry::material),
                        HTCodecs.POSITIVE_INT
                            .fieldOf(HTConst.COUNT)
                            .orElse(1)
                            .forGetter(MaterialPartEntry::count),
                    ).apply(instance, ::MaterialPartEntry)
            }

            @JvmField
            val TYPE: EntryType<MaterialPartEntry> = EntryType(CODEC)
        }

        constructor(part: HTPartLike, material: HTMaterialLike, count: Int) : this(part.asPart(), material.asMaterialKey(), count)

        override fun type(): EntryType<*> = TYPE

        override fun get(): HTTextResult<ItemStack> {
            val tagResult: HTTextResult<ItemStack>? = part.tagPrefix
                ?.itemTagKey(material)
                ?.let { HiiragiCoreAccess.INSTANCE.getFirstHolder(null, it) }
                ?.map { ItemStack(it.get(), count) }
            if (tagResult != null && tagResult.value() != null) {
                return tagResult
            }
            return HiiragiCoreAccess.INSTANCE
                .getMaterialBlockOrItem(part, material)
                .toResource()
                .toStackOrEmpty(count)
                .let(HTItemResult::SimpleEntry)
                .get()
        }

        override fun getId(): ResourceLocation = part.createId(material)
    }
}
