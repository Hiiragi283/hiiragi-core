package hiiragi283.lib.recipe

/**
 * レシピの変換部分を切り出したインターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @param OUTPUT レシピの出力となるクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTRecipeFactory<INPUT : Any, OUTPUT : Any> {
    /**
     * 指定された[input]から完成品を作成します。
     */
    fun assemble(input: INPUT): OUTPUT
}
