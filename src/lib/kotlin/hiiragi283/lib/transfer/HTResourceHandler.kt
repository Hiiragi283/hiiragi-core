package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * [HTResourceSlot]に基づいた[ResourceHandler]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - IMekanismResourceHandler](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/resource/IMekanismResourceHandler.java)
 * @param RESOURCE 保持するリソースのクラス
 * @param SLOT 保持する[HTResourceSlot]のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTResourceHandler<RESOURCE : Resource, SLOT : HTResourceSlot<RESOURCE>> : ResourceHandler<RESOURCE> {
    /**
     * スロットの一覧
     */
    fun getSlots(): List<SLOT>

    /**
     * スロットを取得します。
     * @param index スロットのインデックス
     * @throws IndexOutOfBoundsException [index]が[getSlots]の範囲外の場合
     */
    fun getSlot(index: Int): SLOT = getSlots()[index]

    override fun size(): Int = getSlots().size

    override fun getResource(index: Int): RESOURCE = getSlot(index).resource

    override fun getAmountAsLong(index: Int): Long = getSlot(index).amountAsLong

    @Suppress("NonExtendableApiUsage")
    override fun getAmountAsInt(index: Int): Int = getSlot(index).amountAsInt

    override fun getCapacityAsLong(index: Int, resource: RESOURCE): Long = getSlot(index).getCapacityAsLong(resource)

    @Suppress("NonExtendableApiUsage")
    override fun getCapacityAsInt(index: Int, resource: RESOURCE): Int = getSlot(index).getCapacityAsInt(resource)

    override fun isValid(index: Int, resource: RESOURCE): Boolean = getSlot(index).isValid(resource)

    override fun insert(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = this.insert(index, resource, amount, transaction, HTHandlerAccess.EXTERNAL)

    override fun extract(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = this.extract(index, resource, amount, transaction, HTHandlerAccess.EXTERNAL)

    /**
     * リソースを指定したスロットに搬入します。
     * @param index 搬入するスロットのインデックス
     * @param resource 搬入するリソース
     * @param amount 搬入する量
     * @param transaction 現在のトランザクション
     * @param access スロットへのアクセス状態
     * @return 搬入可能な量
     */
    fun insert(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int = getSlot(index).insert(resource, amount, transaction, access)

    /**
     * リソースをすべてのスロットに対して搬入します。
     * @param resource 搬入するリソース
     * @param amount 搬入する量
     * @param transaction 現在のトランザクション
     * @param access スロットへのアクセス状態
     * @return 搬入可能な量
     */
    fun insert(resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        var inserted = 0
        for (i: Int in this.indices) {
            inserted += insert(i, resource, amount - inserted, transaction, access)
            if (inserted == amount) break
        }
        return inserted
    }

    /**
     * リソースを指定したスロットから搬出します。
     * @param index 搬出するスロットのインデックス
     * @param resource 搬出するリソース
     * @param amount 搬出する量
     * @param transaction 現在のトランザクション
     * @param access スロットへのアクセス状態
     * @return 搬出可能な量
     */
    fun extract(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int = getSlot(index).extract(resource, amount, transaction, access)

    /**
     * リソースをすべてのスロットから搬出します。
     * @param resource 搬出するリソース
     * @param amount 搬出する量
     * @param transaction 現在のトランザクション
     * @param access スロットへのアクセス状態
     * @return 搬出可能な量
     */
    fun extract(resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        var extracted = 0
        for (i: Int in this.indices) {
            extracted += extract(i, resource, amount - extracted, transaction, access)
            if (extracted == amount) break
        }
        return extracted
    }
}
