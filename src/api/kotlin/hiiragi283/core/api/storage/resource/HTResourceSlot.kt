package hiiragi283.core.api.storage.resource

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction

/**
 * リソースを搬入/搬出できることを表すインターフェースです。
 * @param RESOURCE 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTResourceSlot<RESOURCE : HTResourceType<*>> :
    HTResourceView<RESOURCE>,
    HTValueSerializable,
    HTContentListener {
    /**
     * 指定した[resource]が有効か判定します。
     * @return 有効な場合は`true`
     */
    fun isValid(resource: RESOURCE): Boolean

    /**
     * このスロットにリソースを搬入します。
     * @param resource 搬入するリソース
     * @param amount 搬入する量
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬入されない数量
     */
    fun insert(
        resource: RESOURCE?,
        amount: Int,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int

    /**
     * このスロットからリソースを搬出します。
     * @param resource 搬出するリソース
     * @param amount 搬出する量
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬出される数量
     */
    fun extract(
        resource: RESOURCE?,
        amount: Int,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int = when (resource) {
        null -> 0
        getResource() -> extract(amount, action, access)
        else -> 0
    }

    /**
     * このスロットからリソースを搬出します。
     * @param amount 搬出する量
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬出される数量
     */
    fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int

    //    Mutable    //

    abstract class Mutable<RESOURCE : HTResourceType<*>> :
        HTResourceView.Mutable<RESOURCE>(),
        HTResourceSlot<RESOURCE> {
        override fun insert(
            resource: RESOURCE?,
            amount: Int,
            action: HTStorageAction,
            access: HTStorageAccess,
        ): Int {
            if (resource == null || amount <= 0) return 0
            val needed: Int = minOf(inputRate(access), getNeeded(resource))
            if (needed <= 0 || !isStackValidForInsert(resource, access)) return amount

            val resourceIn: RESOURCE? = this.getResource()
            val sameType: Boolean = resource == resourceIn
            if (resourceIn == null || sameType) {
                val toAdd: Int = minOf(amount, needed)
                if (action.execute()) {
                    if (sameType) {
                        growAmount(toAdd)
                        onContentsChanged()
                    } else {
                        setResource(resource)
                        setAmount(toAdd)
                    }
                }
                return amount - toAdd
            }
            return amount
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            val resourceIn: RESOURCE? = this.getResource()
            if (resourceIn == null || amount < 1 || !canStackExtract(resourceIn, access)) return 0
            val fixedAmount: Int = minOf(minOf(outputRate(access), getAmount()), amount)
            if (fixedAmount > 0 && action.execute()) {
                shrinkAmount(fixedAmount)
                onContentsChanged()
            }
            return fixedAmount
        }

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
    }
}
