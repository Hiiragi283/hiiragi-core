package hiiragi283.lib.transfer.fluid

import hiiragi283.lib.transfer.HTStackResourceSlot
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource

/**
 * 液体向けの[HTStackResourceSlot]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
abstract class HTFluidStackResourceSlot : HTStackResourceSlot<FluidResource, FluidStack>() {
    override fun getResourceFrom(stack: FluidStack): FluidResource = FluidResource.of(stack)

    override fun getAmountFrom(stack: FluidStack): Int = stack.amount

    override fun isSame(stack: FluidStack, resource: FluidResource): Boolean = getResourceFrom(stack) == resource

    override fun createStack(resource: FluidResource, amount: Int): FluidStack = resource.toStack(amount)
}
