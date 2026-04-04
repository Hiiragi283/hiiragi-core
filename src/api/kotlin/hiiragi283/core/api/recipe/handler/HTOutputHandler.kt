package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.transfer.useTransaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * レシピの出力スロットを表すインターフェースです。
 * @param STACK 完成品のクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.outputs.IOutputHandler
 */
interface HTOutputHandler<STACK : Any> {
    fun getResultAmount(stack: STACK): Int

    /**
     * 指定した[完成品][stack]を搬入可能かどうか判定します。
     * @return 過不足なく搬入できる場合は`true`
     */
    fun canInsert(stack: STACK, transaction: TransactionContext? = null): Boolean = useTransaction(transaction) {
        insert(stack, it) == getResultAmount(stack)
    }

    /**
     * 指定した[完成品][stack]を搬入します。
     * @return 搬入される量
     */
    fun insert(stack: STACK, transaction: TransactionContext): Int
}
