package hiiragi283.lib.recipe.handler

import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * レシピの入力スロットを表すインターフェースです。
 * @param STACK 入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
fun interface HTInputHandler<STACK : Any> {
    /**
     * 指定した[数量][amount]だけ中身を消費します。
     * @return 実際に消費される数量
     */
    fun extract(amount: Int, parent: TransactionContext?): Result<Int>
}
