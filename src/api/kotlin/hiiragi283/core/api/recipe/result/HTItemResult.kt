package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.compareTo
import hiiragi283.core.api.function.identityRight
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.util.Ior
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
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
        @JvmStatic
        fun checkTagHolderSet(holderSet: HolderSet<Item>) {
            check(holderSet.unwrapKey().isPresent) { "HTItemResult only supports HolderSet with tag" }
        }

        @JvmStatic
        private val HOLDER_SET_CODEC: BiCodec<RegistryFriendlyByteBuf, HolderSet<Item>> = VanillaBiCodecs
            .holderSet(Registries.ITEM)
            .validate { holderSet: HolderSet<Item> ->
                checkTagHolderSet(holderSet)
                holderSet
            }

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = BiCodec.composite(
            MapBiCodecs
                .ior(HTItemResourceType.CODEC.toMap(), HOLDER_SET_CODEC.fieldOf(HTConst.TAG))
                .forGetter(HTItemResult::content),
            BiCodecs.POSITIVE_INT
                .optionalFieldOf(HTConst.COUNT, 1)
                .forGetter(HTItemResult::count),
            BiCodecs
                .fractionRange(Fraction.ZERO..Fraction.ONE)
                .optionalFieldOf(HTConst.CHANCE, Fraction.ONE)
                .forGetter(HTItemResult::chance),
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
        fun create(holderSet: HolderSet<Item>, count: Int = 1, chance: Fraction = Fraction.ONE): HTItemResult {
            checkTagHolderSet(holderSet)
            return HTItemResult(Ior.Right(holderSet), count, chance)
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
        { holderSet: HolderSet<Item> -> holderSet.unwrapKey().map(TagKey<Item>::location).orElseThrow() },
        identityRight(),
    )
}
