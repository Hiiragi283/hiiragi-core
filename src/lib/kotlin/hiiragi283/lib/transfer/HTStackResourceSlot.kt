package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * スタックに基づいた[HTResourceSlot]の抽象クラスです。
 * @param STACK リソースと値を束ねたクラス
 * @param T 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
abstract class HTStackResourceSlot<T : Resource, STACK : Any> : HTResourceSlot<T> {
    abstract fun getStack(): STACK

    abstract fun setStack(stack: STACK)

    protected abstract fun setStackInternal(stack: STACK)

    protected abstract fun getResourceFrom(stack: STACK): T

    protected abstract fun getAmountFrom(stack: STACK): Int

    protected abstract fun isSame(stack: STACK, resource: T): Boolean

    protected abstract fun updateAmount(newAmount: Int)

    protected abstract fun createStack(resource: T, amount: Int): STACK

    protected abstract fun onContentsChanged()

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

    //    HTResourceSlot    //

    override fun insert(resource: T, amount: Int, access: HTHandlerAccess, transaction: TransactionContext): Int {
        TODO("Not yet implemented")
    }

    override fun extract(resource: T, amount: Int, access: HTHandlerAccess, transaction: TransactionContext): Int {
        TODO("Not yet implemented")
    }

    override fun getResource(): T = getStack().let(::getResourceFrom)

    override fun getAmountAsLong(): Long = getStack().let(::getAmountFrom).toLong()

    //    StackJournal    //

    private val journal = StackJournal()

    inner class StackJournal : SnapshotJournal<STACK>() {
        override fun createSnapshot(): STACK = getStack()

        override fun revertToSnapshot(snapshot: STACK) {
            setStackInternal(snapshot)
        }

        override fun onRootCommit(originalState: STACK) {
            onContentsChanged()
        }
    }
}
