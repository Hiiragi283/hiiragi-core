package hiiragi283.core.api.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.compareTo
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.util.HTTextResult
import hiiragi283.core.api.util.getOrElse
import hiiragi283.core.api.util.right
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
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
                    .optionalFieldOf(HTConst.CHANCE, Fraction.ONE)
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
        !preview && HiiragiCoreAPI.RANDOM.nextFloat() >= chance -> ItemStack.EMPTY.right()
        else -> base.create()
    }

    fun createOrEmpty(): ItemStack = create(false).getOrElse { ItemStack.EMPTY }

    override fun getId(): ResourceLocation = base.getId()
}
