package hiiragi283.core.api.recipe.ingredient

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

/**
 * [FluidStack]向けに[HTIngredient]を実装したクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
class HTFluidIngredient(val unsized: FluidIngredient, val amount: Int) : HTIngredient<FluidStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidIngredient> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTCodecs.FLUID_INGREDIENT.forGetter(HTFluidIngredient::unsized),
                    HTCodecs.NON_NEGATIVE_INT
                        .fieldOf(HTConst.AMOUNT)
                        .orElse(FluidType.BUCKET_VOLUME)
                        .forGetter(HTFluidIngredient::amount),
                ).apply(instance, ::HTFluidIngredient)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = StreamCodec.composite(
            FluidIngredient.STREAM_CODEC,
            HTFluidIngredient::unsized,
            ByteBufCodecs.VAR_INT,
            HTFluidIngredient::amount,
            ::HTFluidIngredient,
        )
    }

    init {
        require(!unsized.isEmpty) { "Fluid ingredient must not be empty" }
        require(amount > 0) { "Fluid ingredient amount must be positive" }
    }

    override fun test(stack: FluidStack): Boolean = testOnlyType(stack) && stack.amount >= amount

    override fun testOnlyType(stack: FluidStack): Boolean = unsized.test(stack)

    override fun getRequiredAmount(stack: FluidStack): Int = when {
        testOnlyType(stack) -> amount
        else -> 0
    }

    override fun getPreviewStacks(): List<FluidStack> = unsized.stacks.map { it.copyWithAmount(amount) }
}
