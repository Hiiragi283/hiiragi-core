package hiiragi283.core.support.storage.resource

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.resource.HTResourceSlot
import hiiragi283.core.api.storage.resource.HTResourceType

/**
 * スタックに基づいた[HTResourceSlot]の抽象クラスです。
 * @param STACK リソースと値を束ねたクラス
 * @param RESOURCE 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
abstract class HTStackResourceSlot<STACK : Any, RESOURCE : HTResourceType> : HTResourceSlot<RESOURCE> {
    abstract fun getStack(): STACK

    abstract fun setStack(stack: STACK)

    protected abstract fun setStackInternal(stack: STACK)

    protected abstract fun getResourceFrom(stack: STACK): RESOURCE?

    protected abstract fun getAmountFrom(stack: STACK): Int

    protected abstract fun isSame(stack: STACK, resource: RESOURCE): Boolean

    protected abstract fun updateAmount(newAmount: Int)

    protected abstract fun createStack(resource: RESOURCE?, amount: Int): STACK

    /**
     * 指定したリソースをこのスロットに搬入できるか判定します。
     * @param resource 搬入されるリソース
     * @param access このスロットへのアクセスの種類
     * @return 搬入できる場合は`true`
     */
    open fun isStackValidForInsert(resource: RESOURCE, access: HTStorageAccess): Boolean = isValid(resource)

    /**
     * 指定したリソースをこのスロットから搬出できるか判定します。
     * @param resource 搬出されるリソース
     * @param access このスロットへのアクセスの種類
     * @return 搬出できる場合は`true`
     */
    open fun canStackExtract(resource: RESOURCE, access: HTStorageAccess): Boolean = true

    /**
     * 一度に搬入される量の上限を返します。
     * @param access このスロットへのアクセスの種類
     */
    protected open fun inputRate(access: HTStorageAccess): Int = Int.MAX_VALUE

    /**
     * 一度に搬出される量の上限を返します。
     * @param access このスロットへのアクセスの種類
     */
    protected open fun outputRate(access: HTStorageAccess): Int = Int.MAX_VALUE

    //    HTResourceSlot    //

    override fun insert(
        resource: RESOURCE?,
        amount: Int,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int {
        if (resource == null || amount <= 0) return 0
        val needed: Int = minOf(inputRate(access), getNeeded(resource))
        if (needed <= 0 || !isStackValidForInsert(resource, access)) return amount

        val stackIn: STACK = getStack()
        val sameType: Boolean = isSame(stackIn, resource)
        if (getResource() == null || sameType) {
            val toAdd: Int = minOf(amount, needed)
            if (action.execute()) {
                if (sameType) {
                    updateAmount(this.getAmount() + toAdd)
                    onContentsChanged()
                } else {
                    createStack(resource, toAdd).let(::setStackInternal)
                }
            }
            return amount - toAdd
        }
        return amount
    }

    override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
        val resourceIn: RESOURCE? = getResource()
        if (resourceIn == null || amount < 1 || !canStackExtract(resourceIn, access)) return 0
        val fixedAmount: Int = minOf(minOf(outputRate(access), getAmount()), amount)
        if (fixedAmount > 0 && action.execute()) {
            updateAmount(this.getAmount() - fixedAmount)
            onContentsChanged()
        }
        return fixedAmount
    }

    final override fun getResource(): RESOURCE? = getStack().let(::getResourceFrom)

    final override fun getAmount(): Int = getStack().let(::getAmountFrom)
}
