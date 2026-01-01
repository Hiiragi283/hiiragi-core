package hiiragi283.core.api.recipe.ingredient

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient

/**
 * [HTFluidResourceType]向けに[HTIngredient]を実装したクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
@JvmInline
value class HTFluidIngredient(val delegate: SizedFluidIngredient) : HTIngredient<Fluid, HTFluidResourceType> {
    companion object {
        /**
         * [HTFluidIngredient]の[BiCodec]
         */
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = BiCodec.composite(
            VanillaBiCodecs.FLUID_INGREDIENT.forGetter(HTFluidIngredient::ingredient),
            BiCodecs.POSITIVE_INT
                .optionalFieldOf(HTConst.AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)
                .forGetter(HTFluidIngredient::getRequiredAmount),
            ::HTFluidIngredient,
        )
    }

    constructor(ingredient: FluidIngredient, amount: Int) : this(SizedFluidIngredient(ingredient, amount))

    val ingredient: FluidIngredient get() = delegate.ingredient()

    fun test(stack: FluidStack): Boolean {
        val resource: HTFluidResourceType = stack.toResource() ?: return false
        return test(resource, stack.amount)
    }

    fun testOnlyType(stack: FluidStack): Boolean = stack.toResource()?.let(::testOnlyType) ?: false

    fun copyWithAmount(amount: Int): HTFluidIngredient = HTFluidIngredient(ingredient, amount)

    override fun testOnlyType(resource: HTFluidResourceType): Boolean = ingredient.test(resource.toStack(1))

    override fun getRequiredAmount(): Int = delegate.amount()

    override fun unwrap(): Either<TagKey<Fluid>, List<HTFluidResourceType>> = when (ingredient) {
        is TagFluidIngredient -> Either.Left((ingredient as TagFluidIngredient).tag())
        else -> Either.Right(ingredient.stacks.mapNotNull(FluidStack::toResource))
    }
}
