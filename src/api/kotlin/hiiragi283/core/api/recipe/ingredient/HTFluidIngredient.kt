package hiiragi283.core.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.stack.toImmutable
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient

/**
 * [ImmutableFluidStack]向けに[HTIngredient]を実装したクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@JvmRecord
data class HTFluidIngredient(private val ingredient: FluidIngredient, private val amount: Int) : HTIngredient<Fluid, ImmutableFluidStack> {
    companion object {
        /**
         * [HTFluidIngredient]の[BiCodec]
         */
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = BiCodec.composite(
            VanillaBiCodecs.FLUID_INGREDIENT.forGetter(HTFluidIngredient::ingredient),
            BiCodecs.POSITIVE_INT.fieldOf(HTConst.AMOUNT).forGetter(HTFluidIngredient::amount),
            ::HTFluidIngredient,
        )
    }

    fun test(stack: FluidStack): Boolean = stack.toImmutable()?.let(this::test) ?: false

    fun testOnlyType(stack: FluidStack): Boolean = stack.toImmutable()?.let(this::testOnlyType) ?: false

    fun copyWithAmount(amount: Int): HTFluidIngredient = HTFluidIngredient(ingredient, amount)

    override fun testOnlyType(stack: ImmutableFluidStack): Boolean = ingredient.test(stack.unwrap())

    override fun getRequiredAmount(): Int = this.amount

    override fun unwrap(): Either<Pair<TagKey<Fluid>, Int>, List<ImmutableFluidStack>> = when (ingredient) {
        is TagFluidIngredient -> Either.left(ingredient.tag() to amount)
        else -> Either.right(ingredient.stacks.map { it.copyWithAmount(amount) }.mapNotNull(FluidStack::toImmutable))
    }
}
