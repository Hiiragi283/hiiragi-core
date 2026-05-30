package hiiragi283.lib.recipe.handler

import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * レシピの出力スロットを表すインターフェースです。
 * @param STACK 完成品のクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
fun interface HTOutputHandler<STACK : Any> {
    /**
     * 指定した[完成品][stack]を搬入します。
     * @return 実際に搬入される数量
     */
    fun insert(stack: STACK, transaction: TransactionContext): Result<Int>
}
