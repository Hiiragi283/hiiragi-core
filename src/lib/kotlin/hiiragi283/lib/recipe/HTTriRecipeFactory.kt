package hiiragi283.lib.recipe

/**
 * レシピの変換部分を切り出したインターフェースです。
 * @param INPUT_A 1番目のレシピの入力となるクラス
 * @param INPUT_B 2番目のレシピの入力となるクラス
 * @param INPUT_C 3番目のレシピの入力となるクラス
 * @param OUTPUT レシピの出力となるクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTTriRecipeFactory<INPUT_A : Any, INPUT_B : Any, INPUT_C : Any, OUTPUT : Any> {
    /**
     * 指定された[firstInput]と[secondInput]，[thirdInput]から完成品を作成します。
     */
    fun assemble(firstInput: INPUT_A, secondInput: INPUT_B, thirdInput: INPUT_C): OUTPUT
}
