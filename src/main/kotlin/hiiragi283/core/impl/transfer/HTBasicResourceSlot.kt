package hiiragi283.core.impl.transfer

import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.HTResourceSlot
import hiiragi283.core.api.transfer.HTSlotModifier
import hiiragi283.core.api.transfer.getNeededAsInt
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * @see net.neoforged.neoforge.transfer.StacksResourceHandler
 */
abstract class HTBasicResourceSlot<T : Resource> :
    HTResourceSlot<T>,
    HTSlotModifier<T> {
    override fun insert(
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        val needed: Int = minOf(inputRate(access), getNeededAsInt(resource))
        if (needed <= 0 || !isStackValidForInsert(resource, access)) return amount

        val resourceIn: T = this.resource
        val isSameType: Boolean = resourceIn == resource
        if (resourceIn.isEmpty || isSameType) {
            val inserted: Int = minOf(amount, needed)
            if (inserted > 0) {
                updateSnapshot(transaction)
                if (isSameType) {
                    this.amountAsLong += inserted
                } else {
                    this.resource = resource
                    this.amountAsLong = inserted.toLong()
                }
                return inserted
            }
        }
        return 0
    }

    override fun extract(
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        if (!canStackExtract(resource, access) || resource != this.resource) return 0
        val extracted: Int = minOf(minOf(outputRate(access), this.amountAsInt), amount)
        if (extracted > 0) {
            updateSnapshot(transaction)
            this.amountAsLong -= extracted
            return extracted
        }
        return 0
    }

    protected abstract fun updateSnapshot(transaction: TransactionContext)

    override fun set(resource: T, amount: Int) {
        this.resource = resource
        this.amountAsInt = amount
    }

    abstract override var resource: T
    abstract override var amountAsLong: Long
    final override var amountAsInt: Int
        get() = super.amountAsInt
        set(value) {
            amountAsLong = value.toLong()
        }

    /**
     * 指定したリソースをこのスロットに搬入できるか判定します。
     * @param resource 搬入されるリソース
     * @param access このスロットへのアクセスの種類
     * @return 搬入できる場合は`true`
     */
    open fun isStackValidForInsert(resource: T, access: HTHandlerAccess): Boolean = isValid(resource)

    /**
     * 指定したリソースをこのスロットから搬出できるか判定します。
     * @param resource 搬出されるリソース
     * @param access このスロットへのアクセスの種類
     * @return 搬出できる場合は`true`
     */
    open fun canStackExtract(resource: T, access: HTHandlerAccess): Boolean = true

    /**
     * 一度に搬入される量の上限を返します。
     * @param access このスロットへのアクセスの種類
     */
    protected open fun inputRate(access: HTHandlerAccess): Int = Int.MAX_VALUE

    /**
     * 一度に搬出される量の上限を返します。
     * @param access このスロットへのアクセスの種類
     */
    protected open fun outputRate(access: HTHandlerAccess): Int = Int.MAX_VALUE
}
