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

    fun getStackOrEmpty(level: LevelAccessor): ItemStack = getStackOrEmpty(level.registryAccess(), level.random)
    
    fun getStackOrEmpty(provider: HolderLookup.Provider, random: RandomSource): ItemStack = when {
        random.nextFloat() <= this.chance.toFloat() -> result.getStackOrEmpty(provider)
        else -> ItemStack.EMPTY
    }
}
