package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.storage.amount.HTAmountView

/**
 * @author Hiiragi Tsubasa
 * @since 0.15.2
 */
interface HTAmountInputHandler : HTAmountView {
    /**
     * 指定した[数量][amount]だけ中身を消費します。
     */
    fun consume(amount: Int)
}
