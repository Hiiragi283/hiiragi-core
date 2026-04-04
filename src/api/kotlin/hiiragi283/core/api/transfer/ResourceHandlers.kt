package hiiragi283.core.api.transfer

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

val ResourceHandler<*>.indices: IntRange get() = (0..<size())

inline fun <R> useTransaction(parent: TransactionContext? = null, action: (Transaction) -> R): R = Transaction.open(parent).use(action)

//    Fluid    //

typealias FluidResourceHandler = ResourceHandler<FluidResource>

fun FluidResourceHandler.getStack(index: Int): FluidStack = this.getResource(index).toStack(this.getAmountAsInt(index))

//    Item    //

typealias ItemResourceHandler = ResourceHandler<ItemResource>

fun ItemResourceHandler.getStack(index: Int): ItemStack = this.getResource(index).toStack(this.getAmountAsInt(index))
