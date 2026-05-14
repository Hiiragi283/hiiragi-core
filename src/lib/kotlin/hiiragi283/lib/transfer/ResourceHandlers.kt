package hiiragi283.lib.transfer

import hiiragi283.lib.math.fixedFraction
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import org.apache.commons.lang3.math.Fraction

val ResourceHandler<*>.indices: IntRange get() = (0..<size())

fun <T : Resource> ResourceHandler<T>.getFilledLevel(index: Int): Fraction = fixedFraction(this.getAmountAsLong(index), this.getCapacityAsLong(index, this.getResource(index)))

fun <T : Resource> ResourceHandler<T>.asResourceSlots(): List<HTHandlerResourceSlot<T>> = this.indices.map { index: Int -> HTHandlerResourceSlot(this, index) }

//    Transaction    //

inline fun <T> useTransaction(parent: TransactionContext? = null, action: (Transaction) -> T): Result<T> = runCatching { Transaction.open(parent).use(action) }
