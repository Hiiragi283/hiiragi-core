package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

val ResourceHandler<*>.indices: IntRange get() = (0..<size())

//    Transaction    //

inline fun <T> useTransaction(parent: TransactionContext? = null, action: (Transaction) -> T): T = Transaction.open(parent).use(action)
