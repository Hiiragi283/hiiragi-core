package hiiragi283.lib.transfer

import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * リソースを搬入/搬出できることを表すインターフェースです。
 * @param RESOURCE 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTResourceSlot<RESOURCE : Resource> :
    HTResourceView<RESOURCE>,
    ValueIOSerializable {
    fun isValid(resource: RESOURCE): Boolean

    fun insert(resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int

    fun extract(resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int
}
