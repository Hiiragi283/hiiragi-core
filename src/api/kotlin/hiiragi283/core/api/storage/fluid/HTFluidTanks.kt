package hiiragi283.core.api.storage.fluid

import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.stack.toImmutable
import hiiragi283.core.api.storage.stack.HTStackView
import net.neoforged.neoforge.fluids.FluidStack

typealias HTFluidView = HTStackView<ImmutableFluidStack>

fun HTFluidView.getFluidStack(): FluidStack = this.getStack()?.unwrap() ?: FluidStack.EMPTY

fun HTFluidTank.isValid(stack: FluidStack): Boolean = stack.toImmutable()?.let(this::isValid) ?: false
