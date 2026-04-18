package hiiragi283.core.api.recipe.handler

interface HTAmountInputHandler {
    /**
     * 指定した[数量][amount]だけ中身を消費します。
     */
    fun consume(amount: Int)
}
