package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.compareTo
import hiiragi283.core.api.function.identityRight
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.util.Ior
import hiiragi283.core.api.util.toIorOrThrow
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.RandomSource
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelAccessor
import org.apache.commons.lang3.math.Fraction
import java.util.function.IntUnaryOperator

/**
 * [アイテム][ItemStack]の[完成品][HTRecipeResult]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
class HTItemResult(private val content: Ior<HTItemResourceType, TagKey<Item>>, private val count: Int) : HTRecipeResult<ItemStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = BiCodec.composite(
            MapBiCodecs
                .ior(
                    HTItemResourceType.CODEC.toMap(),
                    VanillaBiCodecs.tagKey(Registries.ITEM, false).fieldOf(HTConst.TAG),
                ).forGetter(HTItemResult::content),
            BiCodecs.POSITIVE_INT.optionalFieldOf(HTConst.COUNT, 1).forGetter(HTItemResult::count),
            ::HTItemResult,
        )

        /**
         * @since 0.11.0
         */
        @JvmField
        val CHANCED_CODEC: BiCodec<RegistryFriendlyByteBuf, HTChancedItemResult> = BiCodec.composite(
            CODEC.toMap().forGetter(HTChancedItemResult::first),
            BiCodecs
                .fractionRange(Fraction.ZERO..Fraction.ONE)
                .optionalFieldOf(HTConst.CHANCE, Fraction.ONE)
                .forGetter(HTChancedItemResult::second),
            ::HTChancedItemResult,
        )

        /**
         * [HTItemResult]の新しいインスタンスを作成します。
         * @since 0.8.0
         */
        @HTBuilderMarker
        @JvmStatic
        fun create(builderAction: Builder.() -> Unit): HTItemResult = Builder().apply(builderAction).build()
    }

    fun copyWithCount(count: Int): HTItemResult = HTItemResult(content, count)

    fun copyWithCount(operator: IntUnaryOperator): HTItemResult = HTItemResult(content, operator.applyAsInt(count))

    /**
     * 指定した[レジストリ][provider]から完成品を取得します。
     * @return 完成品を取得できなかった場合は[ItemStack.EMPTY]
     */
    fun getStackOrEmpty(provider: HolderLookup.Provider?): ItemStack = getStackResult(provider).valueOrElse(ItemStack::EMPTY)

    override fun getStackResult(provider: HolderLookup.Provider?): HTTextResult<ItemStack> = content.map(
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

    override fun getId(): ResourceLocation = content.map(HTItemResourceType::getId, TagKey<Item>::location, identityRight())

    override fun toString(): String = "HTItemResult(content=$content, count=$count)"

    //    Builder    //

    /**
     * [HTItemResult]のビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    class Builder {
        var item: HTItemResourceType? = null
        var tagKey: TagKey<Item>? = null
        var amount: Int = 1

        fun build(): HTItemResult = HTItemResult((item to tagKey).toIorOrThrow("Either item or tag required for result"), amount)
    }
}

//    HTChancedItemResult    //

typealias HTChancedItemResult = Pair<HTItemResult, Fraction>

/**
 * 指定した[レベル][level]から完成品を取得します。
 * @return 完成品を取得できなかった場合は`null`
 */
fun HTChancedItemResult.getStackOrNull(level: LevelAccessor): ItemStack? = this.getStackOrNull(level.registryAccess(), level.random)

/**
 * 指定した[レジストリ][provider]と[乱数][random]から完成品を取得します。
 * @return 完成品を取得できなかった場合は`null`
 */
fun HTChancedItemResult.getStackOrNull(provider: HolderLookup.Provider, random: RandomSource): ItemStack? =
    this.getStackOrNull(provider, random.nextFloat())

/**
 * 指定した[レジストリ][provider]と[チャンス][chance]から完成品を取得します。
 * @return 完成品を取得できなかった場合は`null`
 */
fun HTChancedItemResult.getStackOrNull(provider: HolderLookup.Provider, chance: Float): ItemStack? = when {
    chance <= this.second -> this.first.getStackResult(provider).value()
    else -> null
}
