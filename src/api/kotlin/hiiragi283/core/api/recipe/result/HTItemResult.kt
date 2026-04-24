package hiiragi283.core.api.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.compareTo
import hiiragi283.core.api.function.identityRight
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.util.Ior
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import org.apache.commons.lang3.math.Fraction

@JvmRecord
data class HTItemResult(private val content: Ior<HTItemResourceType, HolderSet<Item>>, val count: Int, val chance: Fraction) :
    HTRecipeResult<ItemStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemResult> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTCodecs
                        .ior(HTItemResourceType.MAP_CODEC, HTCodecs.holderSet(Registries.ITEM).fieldOf(HTConst.TAG))
                        .forGetter(HTItemResult::content),
                    HTCodecs.POSITIVE_INT
                        .optionalFieldOf(HTConst.COUNT, 1)
                        .forGetter(HTItemResult::count),
                    HTCodecs.FRACTION
                        .validate(Codec.checkRange(Fraction.ZERO, Fraction.ONE))
                        .optionalFieldOf(HTConst.CHANCE, Fraction.ONE)
                        .forGetter(HTItemResult::chance),
                ).apply(instance, ::HTItemResult)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> = StreamCodec.composite(
            HTStreamCodecs.ior(HTItemResourceType.STREAM_CODEC, HTStreamCodecs.holderSet(Registries.ITEM)),
            HTItemResult::content,
            ByteBufCodecs.VAR_INT,
            HTItemResult::count,
            HTStreamCodecs.FRACTION,
            HTItemResult::chance,
            ::HTItemResult,
        )

        @JvmStatic
        fun create(item: ItemLike, count: Int = 1, chance: Fraction = Fraction.ONE): HTItemResult = create(ItemStack(item, count), chance)

        @JvmStatic
        fun create(stack: ItemStack, chance: Fraction = Fraction.ONE): HTItemResult {
            val resource: HTItemResourceType = stack.toResource() ?: error("Cannot create HTItemResult from empty stack")
            return HTItemResult(Ior.Left(resource), stack.count, chance)
        }

        @JvmStatic
        fun create(holderSet: HolderSet<Item>, count: Int = 1, chance: Fraction = Fraction.ONE): HTItemResult =
            HTItemResult(Ior.Right(holderSet), count, chance)
    }

    init {
        content.getRight()?.let {
            check(it.unwrapKey().isPresent) { "HTItemResult only supports HolderSet with tag" }
        }
    }

    fun getOrEmpty(): ItemStack = get().valueOrElse(ItemStack::EMPTY)

    fun getOrEmpty(preview: Boolean): ItemStack = get(preview).valueOrElse(ItemStack::EMPTY)

    override fun get(): HTTextResult<ItemStack> = get(true)

    fun get(preview: Boolean): HTTextResult<ItemStack> = when {
        !preview && HiiragiCoreAPI.RANDOM.nextFloat() >= this.chance -> HTTextResult.success(ItemStack.EMPTY)
        else -> {
            content.map(
                { resource: HTItemResourceType -> HTTextResult.success(resource.toStack(count)) },
                { holderSet: HolderSet<Item> ->
                    HiiragiCoreAccess.INSTANCE
                        .getFirstHolder(holderSet)
                        .map { ItemStack(it.toItemLike(), count) }
                },
                { itemResult: HTTextResult<ItemStack>, tagResult: HTTextResult<ItemStack> ->
                    tagResult.mapOrElse(HTTextResult.Companion::success) { _ -> itemResult }
                },
            )
        }
    }

    override fun getId(): ResourceLocation = content.map(
        HTItemResourceType::getId,
        { holderSet: HolderSet<Item> ->
            holderSet
                .unwrapKey()
                .map(TagKey<Item>::location)
                .orElseThrow { error("Cannot get result id from non-tag holder set") }
        },
        identityRight(),
    )
}
