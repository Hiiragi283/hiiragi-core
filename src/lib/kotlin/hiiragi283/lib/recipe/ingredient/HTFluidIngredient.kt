package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.Codec
import net.minecraft.core.TypedInstance
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.context.ContextMap
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.fluids.crafting.display.ForFluidStacks

@JvmInline
value class HTFluidIngredient(private val delegate: SizedFluidIngredient) : HTIngredient<Fluid, FluidStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidIngredient> = SizedFluidIngredient.CODEC.xmap(::HTFluidIngredient, HTFluidIngredient::delegate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = SizedFluidIngredient.STREAM_CODEC.map(::HTFluidIngredient, HTFluidIngredient::delegate)
    }

    val unsized: FluidIngredient get() = delegate.ingredient()
    val amount: Int get() = delegate.amount()

    override fun test(instance: TypedInstance<Fluid>): Boolean = HTIngredientHelper.createStack(instance).let(delegate::test)

    override fun testOnlyType(instance: TypedInstance<Fluid>): Boolean = HTIngredientHelper.createStack(instance).let(unsized::test)

    override fun getRequiredAmount(instance: TypedInstance<Fluid>): Int = when (testOnlyType(instance)) {
        true -> amount
        false -> 0
    }

    override fun getPreviewStacks(contextMap: ContextMap): List<FluidStack> = unsized
        .display()
        .resolve(contextMap, ForFluidStacks { it.copyWithAmount(amount) })
        .toList()
}
