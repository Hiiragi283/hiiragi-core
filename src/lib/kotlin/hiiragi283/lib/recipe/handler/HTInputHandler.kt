package hiiragi283.lib.recipe.handler

import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * レシピの入力スロットを表すインターフェースです。
 * @param STACK 入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
interface HTInputHandler<STACK : Any> {
    /**
     * 指定した[数量][amount]だけ中身を消費します。
     */
    fun consume(amount: Int, parent: TransactionContext?)

    fun getStack(): STACK
}
