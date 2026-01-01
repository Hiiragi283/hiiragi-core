package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelAccessor
import org.apache.commons.lang3.math.Fraction

/**
 * 確率付きの完成品を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.basic.BasicSawmillRecipe.BasicChanceOutput
 */
data class HTChancedItemResult(val result: HTItemResult, val chance: Fraction) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTChancedItemResult> = BiCodec.composite(
            HTItemResult.CODEC.toMap().forGetter(HTChancedItemResult::result),
            BiCodecs
                .fractionRange(Fraction.ZERO, Fraction.ONE)
                .optionalFieldOf(HTConst.CHANCE, Fraction.ONE)
                .forGetter(HTChancedItemResult::chance),
            ::HTChancedItemResult,
        )
    }

    /**
     * 指定した[レベル][level]から完成品を取得します。
     * @return 完成品を取得できなかった場合は[ItemStack.EMPTY]
     */
    fun getStackOrEmpty(level: LevelAccessor): ItemStack = getStackOrEmpty(level.registryAccess(), level.random)

    /**
     * 指定した[レジストリ][provider]と[乱数][random]から完成品を取得します。
     * @return 完成品を取得できなかった場合は[ItemStack.EMPTY]
     */
    fun getStackOrEmpty(provider: HolderLookup.Provider, random: RandomSource): ItemStack = getStackOrEmpty(provider, random.nextFloat())

    /**
     * 指定した[レジストリ][provider]と[チャンス][chance]から完成品を取得します。
     * @return 完成品を取得できなかった場合は[ItemStack.EMPTY]
     */
    fun getStackOrEmpty(provider: HolderLookup.Provider, chance: Float): ItemStack = when {
        chance <= this.chance.toFloat() -> result.getStackOrEmpty(provider)
        else -> ItemStack.EMPTY
    }
}
