package hiiragi283.lib.transfer

import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * 中身を搬入/搬出可能な[HTResourceView]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - IResourceContainer](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/resource/IResourceContainer.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTResourceSlot<RESOURCE : Resource> :
    HTResourceView<RESOURCE>,
    ValueIOSerializable {
    /**
     * 搬入可能なリソースかどうか判定します。
     */
    fun isValid(resource: RESOURCE): Boolean

    /**
     * リソースをこのスロットに搬入します。
     * @param resource 搬入するリソース
     * @param amount 搬入する量
     * @param transaction 現在のトランザクション
     * @param access スロットへのアクセス状態
     * @return 搬入可能な量
     */
    fun insert(resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int

    /**
     * リソースをこのスロットに搬入します。
     * @param resource 搬出するリソース
     * @param amount 搬出する量
     * @param transaction 現在のトランザクション
     * @param access スロットへのアクセス状態
     * @return 搬出可能な量
     */
    fun extract(resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int
}
