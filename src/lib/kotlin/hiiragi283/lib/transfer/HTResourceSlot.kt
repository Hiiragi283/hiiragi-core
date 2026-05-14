package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * リソースを搬入/搬出できることを表すインターフェースです。
 * @param T 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTResourceSlot<T : Resource> : HTResourceView<T> {
    fun isValid(resource: T): Boolean

    fun insert(resource: T, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int

    fun extract(resource: T, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int
}
