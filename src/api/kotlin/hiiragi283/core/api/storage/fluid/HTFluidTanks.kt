package hiiragi283.core.api.storage.fluid

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.resource.HTResourceView
import net.neoforged.neoforge.fluids.FluidStack

typealias HTFluidView = HTResourceView<HTFluidResourceType>

/**
 * この[HTFluidView][this]から[FluidStack]を取得します。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun HTFluidView.getFluidStack(): FluidStack = this.getResource()?.toStack(this.getAmount()) ?: FluidStack.EMPTY

/**
 * この[HTFluidTank][this]に指定した[stack]を搬入します。
 * @return 搬入されない[FluidStack]
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun HTFluidTank.insert(stack: FluidStack, action: HTStorageAction, access: HTStorageAccess): FluidStack {
    val remainder: Int = this.insert(stack.toResource(), stack.amount, action, access)
    return stack.copyWithAmount(remainder)
}

/**
 * この[HTFluidTank][this]から指定した[stack]を搬出します。
 * @return 搬出される[FluidStack]
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun HTFluidTank.extractFluid(stack: FluidStack, action: HTStorageAction, access: HTStorageAccess): FluidStack =
    this.extract(stack.toResource(), stack.amount, action, access).let(stack::copyWithAmount)

/**
 * この[HTFluidTank][this]から指定した[amount]だけ搬出します。
 * @return 搬出される[FluidStack]
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun HTFluidTank.extractFluid(amount: Int, action: HTStorageAction, access: HTStorageAccess): FluidStack {
    val resourceIn: HTFluidResourceType = this.getResource() ?: return FluidStack.EMPTY
    return this.extract(amount, action, access).let(resourceIn::toStack)
}
