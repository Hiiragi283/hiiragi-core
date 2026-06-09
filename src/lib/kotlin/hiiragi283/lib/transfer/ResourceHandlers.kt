@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.transfer

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

val ResourceHandler<*>.indices: IntRange get() = (0..<size())

//    Transaction    //

inline fun <T> useTransaction(parent: TransactionContext? = null, action: (Transaction) -> T): T {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    return Transaction.open(parent).use(action)
}
