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
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import org.apache.commons.lang3.math.Fraction

/**
 * [アイテム][ItemStack]の[完成品][HTRecipeResult]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
@JvmRecord
data class HTItemResult(private val content: Ior<HTItemResourceType, TagKey<Item>>, val count: Int, val chance: Fraction) :
    HTRecipeResult<ItemStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = BiCodec.composite(
            MapBiCodecs
                .ior(
                    HTItemResourceType.CODEC.toMap(),
                    VanillaBiCodecs.tagKey(Registries.ITEM, false).fieldOf(HTConst.TAG),
                ).forGetter(HTItemResult::content),
            BiCodecs.POSITIVE_INT.optionalFieldOf(HTConst.COUNT, 1).forGetter(HTItemResult::count),
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
        fun create(tagKey: TagKey<Item>, count: Int = 1, chance: Fraction = Fraction.ONE): HTItemResult =
            HTItemResult(Ior.Right(tagKey), count, chance)
    }

    /**
     * 指定した[レジストリ][provider]から完成品を取得します。
     * @return 完成品を取得できなかった場合は[ItemStack.EMPTY]
     */
    fun getStackOrEmpty(provider: HolderLookup.Provider?): ItemStack = getStackResult(provider).valueOrElse(ItemStack::EMPTY)

    /**
     * @since 0.15.0
     */
    fun getStackOrEmpty(provider: HolderLookup.Provider?, useChance: Boolean): ItemStack =
        getStackResult(provider, useChance).valueOrElse(ItemStack::EMPTY)

    override fun getStackResult(provider: HolderLookup.Provider?): HTTextResult<ItemStack> = getStackResult(provider, true)

    /**
     * @since 0.15.0
     */
    fun getStackResult(provider: HolderLookup.Provider?, useChance: Boolean): HTTextResult<ItemStack> = when {
        useChance && HiiragiCoreAPI.RANDOM.nextFloat() >= this.chance -> HTTextResult.success(ItemStack.EMPTY)
        else ->
            content.map(
                { resource: HTItemResourceType -> HTTextResult.success(resource.toStack(count)) },
                { tagKey: TagKey<Item> ->
                    HiiragiCoreAccess.INSTANCE
                        .getFirstHolder(provider, tagKey)
                        .map { ItemStack(it.toItemLike(), count) }
                },
                { itemResult: HTTextResult<ItemStack>, tagResult: HTTextResult<ItemStack> ->
                    tagResult.mapOrElse(HTTextResult.Companion::success) { _ -> itemResult }
                },
            )
    }

    override fun getId(): ResourceLocation = content.map(HTItemResourceType::getId, TagKey<Item>::location, identityRight())
}
