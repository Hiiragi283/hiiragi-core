package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.compareTo
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelAccessor
import org.apache.commons.lang3.math.Fraction
import java.util.Optional

/**
 * 確率付きの完成品を表すクラスです。
 * @param chance 完成品を出力する確率
 * @param fallback 確率が外れた時の代替品
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.basic.BasicSawmillRecipe.BasicChanceOutput
 */
data class HTChancedItemResult(val result: HTItemResult, val chance: Fraction, val fallback: Optional<HTItemResult>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTChancedItemResult> = BiCodec.composite(
            HTItemResult.CODEC.toMap().forGetter(HTChancedItemResult::result),
            BiCodecs
                .fractionRange(Fraction.ZERO, Fraction.ONE)
                .optionalFieldOf(HTConst.CHANCE, Fraction.ONE)
                .forGetter(HTChancedItemResult::chance),
            HTItemResult.CODEC.optionalFieldOf("fallback").forGetter(HTChancedItemResult::fallback),
            ::HTChancedItemResult,
        )

        @HTBuilderMarker
        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTChancedItemResult = Builder().apply(builderAction).build()
    }

    /**
     * 指定した[レベル][level]から完成品を取得します。
     * @return 完成品を取得できなかった場合は[fallback]の戻り値
     */
    fun getStackOrEmpty(level: LevelAccessor): ItemStack = getStackOrEmpty(level.registryAccess(), level.random)

    /**
     * 指定した[レジストリ][provider]と[乱数][random]から完成品を取得します。
     * @return 完成品を取得できなかった場合は[fallback]の戻り値
     */
    fun getStackOrEmpty(provider: HolderLookup.Provider, random: RandomSource): ItemStack = getStackOrEmpty(provider, random.nextFloat())

    /**
     * 指定した[レジストリ][provider]と[チャンス][chance]から完成品を取得します。
     * @return 完成品を取得できなかった場合は[fallback]の戻り値
     */
    fun getStackOrEmpty(provider: HolderLookup.Provider, chance: Float): ItemStack = when {
        chance <= this.chance -> result.getStackOrEmpty(provider)
        else -> fallback.map { it.getStackOrEmpty(provider) }.orElseGet(ItemStack::EMPTY)
    }

    //    Builder    //

    /**
     * [HTChancedItemResult]のビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    class Builder {
        lateinit var result: HTItemResult
        var chance: Fraction = Fraction.ONE
        var fallback: HTItemResult? = null

        fun build(): HTChancedItemResult = HTChancedItemResult(result, chance, Optional.ofNullable(fallback))
    }
}
