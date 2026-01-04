package hiiragi283.core.api.recipe.handler

/**
 * レシピの出力スロットを表すインターフェースです。
 * @param STACK 完成品のクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.outputs.IOutputHandler
 */
interface HTOutputHandler<STACK : Any> {
    /**
     * 指定した[完成品][stack]を搬入可能かどうか判定します。
     * @return 過不足なく搬入できる場合は`true`
     */
    fun canInsert(stack: STACK): Boolean

    /**
     * 指定した[完成品][stack]を搬入します。
     */
    fun insert(stack: STACK)
}
