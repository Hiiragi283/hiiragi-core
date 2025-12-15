package hiiragi283.core.api.stack

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.chat.Component
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

@JvmInline
value class ImmutableFluidStack private constructor(private val stack: FluidStack) : ImmutableComponentStack<Fluid, ImmutableFluidStack> {
    companion object {
        @JvmStatic
        fun ofNullable(fluid: Fluid, amount: Int): ImmutableFluidStack? = FluidStack(fluid, amount).toImmutable()

        @JvmStatic
        fun of(fluid: Fluid, amount: Int): ImmutableFluidStack = FluidStack(fluid, amount).toImmutableOrThrow()

        @JvmStatic
        fun of(stack: FluidStack): ImmutableFluidStack? = when {
            stack.isEmpty -> null
            else -> ImmutableFluidStack(stack)
        }
    }

    fun unwrap(): FluidStack = stack.copy()

    fun fluidType(): FluidType = stack.fluidType

    //    ImmutableComponentStack    //

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun getType(): Fluid = stack.fluid

    override fun getAmount(): Int = stack.amount

    override fun copyWithAmount(amount: Int): ImmutableFluidStack? {
        TODO("Not yet implemented")
    }

    override fun getText(): Component = stack.hoverName

    override fun getHolder(): Holder<Fluid> = stack.fluidHolder

    override fun getComponents(): DataComponentMap = stack.components
}
