package hiiragi283.core.api.recipe.ingredient

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.util.Either
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient

/**
 * [HTFluidResourceType]向けに[HTIngredient]を実装したクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
class HTFluidIngredient(val unsized: FluidIngredient, override val amount: Int) : HTIngredient<Fluid, HTFluidResourceType> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = BiCodec.composite(
            VanillaBiCodecs.FLUID_INGREDIENT.forGetter(HTFluidIngredient::unsized),
            BiCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.AMOUNT).forGetter(HTFluidIngredient::amount),
            ::HTFluidIngredient,
        )
    }

    fun test(stack: FluidStack): Boolean {
        val resource: HTFluidResourceType = stack.toResource() ?: return false
        return test(resource, stack.amount)
    }

    fun testOnlyType(stack: FluidStack): Boolean = stack.toResource()?.let(::testOnlyType) ?: false

    //    HTIngredientN    //

    override fun testOnlyType(resource: HTFluidResourceType): Boolean = unsized.test(resource.toStack(HTConst.DEFAULT_FLUID_AMOUNT))

    override fun unwrap(): Either<TagKey<Fluid>, List<HTFluidResourceType>> = when (unsized) {
        is TagFluidIngredient -> Either.Left(unsized.tag())
        else -> Either.Right(unsized.stacks.mapNotNull(FluidStack::toResource))
    }
}
