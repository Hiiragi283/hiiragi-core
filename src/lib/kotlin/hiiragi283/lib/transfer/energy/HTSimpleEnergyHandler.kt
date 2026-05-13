package hiiragi283.lib.transfer.energy

import hiiragi283.lib.transfer.HTHandlerAccess
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

open class HTSimpleEnergyHandler(capacity: Int, maxInsert: Int = capacity, maxExtract: Int = maxInsert, energy: Int = 0) :
    SimpleEnergyHandler(capacity, maxInsert, maxExtract, energy),
    HTEnergyHandler {
    /**
     * このハンドラに搬入できるか判定します。
     * @param access このハンドラへのアクセスの種類
     * @return 搬入できる場合は`true`
     */
    protected open fun canInsert(access: HTHandlerAccess): Boolean = true

    /**
     * このハンドラから搬出できるか判定します。
     * @param access このハンドラへのアクセスの種類
     * @return 搬出できる場合は`true`
     */
    protected open fun canExtract(access: HTHandlerAccess): Boolean = true

    override fun insert(amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int = when {
        canInsert(access) -> super<SimpleEnergyHandler>.insert(amount, transaction)
        else -> 0
    }

    override fun insert(amount: Int, transaction: TransactionContext): Int = super<HTEnergyHandler>.insert(amount, transaction)

    override fun extract(amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int = when {
        canExtract(access) -> super<SimpleEnergyHandler>.extract(amount, transaction)
        else -> 0
    }

    override fun extract(amount: Int, transaction: TransactionContext): Int = super<HTEnergyHandler>.extract(amount, transaction)
}
