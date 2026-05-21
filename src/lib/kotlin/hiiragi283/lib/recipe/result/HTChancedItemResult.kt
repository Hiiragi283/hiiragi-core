package hiiragi283.lib.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.getOrElse
import hiiragi283.lib.util.right
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import org.apache.commons.lang3.math.Fraction

@JvmRecord
data class HTChancedItemResult(val base: HTItemResult, val chance: Fraction) : HTIdLike {
    companion object {
        @JvmField
        val CODEC: Codec<HTChancedItemResult> = RecordCodecBuilder.create { instance ->
            instance.group(
                HTItemResult.MAP_CODEC.forGetter(HTChancedItemResult::base),
                HTCodecs.FRACTION
                    .validate(Codec.checkRange(Fraction.ZERO, Fraction.ONE))
                    .optionalFieldOf(HTConstants.CHANCE, Fraction.ONE)
                    .forGetter(HTChancedItemResult::chance),
            ).apply(instance, ::HTChancedItemResult)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTChancedItemResult> = StreamCodec.composite(
            HTItemResult.STREAM_CODEC,
            HTChancedItemResult::base,
            HTStreamCodecs.FRACTION,
            HTChancedItemResult::chance,
            ::HTChancedItemResult,
        )
    }

    fun create(preview: Boolean): HTTextResult<ItemStack> = when {
        !preview && HTConstants.RANDOM.nextFloat() >= chance.toFloat() -> ItemStack.EMPTY.right()
        else -> base.create()
    }

    fun createOrEmpty(): ItemStack = create(false).getOrElse { ItemStack.EMPTY }

    override fun getId(): Identifier = base.getId()
}
