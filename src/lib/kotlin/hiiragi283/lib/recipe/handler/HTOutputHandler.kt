package hiiragi283.lib.recipe.handler

import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * レシピの出力スロットを表すインターフェースです。
 *
 * 参照 : [Mekanism - IOutputHandler](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/recipes/inputs/IOutputHandler.java)
 * @param STACK 出力となるクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTOutputHandler<STACK : Any> {
    /**
     * 指定した[完成品][stack]を搬入します。
     * @return 実際に搬入される数量
     */
    fun insert(stack: STACK, transaction: TransactionContext): Result<Int>
}
