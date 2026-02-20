package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.compareTo
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.monad.toIorOrThrow
import net.minecraft.core.HolderLookup
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelAccessor
import org.apache.commons.lang3.math.Fraction

typealias HTComplexResult = Ior<HTItemResult, HTFluidResult>

fun Pair<HTItemResult?, HTFluidResult?>.toComplex(): HTComplexResult = this.toIorOrThrow("Either item or fluid result required")

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
