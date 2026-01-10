package hiiragi283.core.common.storage.fluid

import hiiragi283.core.api.HTConst
import hiiragi283.core.util.HTMoltenMetalHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

/**
 * @see net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper
 */
class HTMoltenMetalBucketHandler(private var stack: ItemStack) : IFluidHandlerItem {
    override fun getContainer(): ItemStack = stack

    override fun getTanks(): Int = 1

    override fun getFluidInTank(tank: Int): FluidStack = when (tank) {
        0 -> HTMoltenMetalHelper.createFluid(stack)
        else -> FluidStack.EMPTY
    }

    override fun getTankCapacity(tank: Int): Int = when (tank) {
        0 -> HTConst.DEFAULT_FLUID_AMOUNT
        else -> 0
    }

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = false

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = 0

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        val stackIn: FluidStack = getFluidInTank(0)
        if (FluidStack.isSameFluidSameComponents(stackIn, resource)) {
            return drain(resource.amount, action)
        }
        return FluidStack.EMPTY
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (stack.isEmpty || stack.count > 1 || maxDrain < HTConst.DEFAULT_FLUID_AMOUNT) {
            return FluidStack.EMPTY
        }
        val stackIn: FluidStack = getFluidInTank(0)
        if (action.execute()) {
            stack = ItemStack(Items.BUCKET)
        }
        return stackIn
    }
}
