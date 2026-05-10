package hiiragi283.lib.recipe

/**
 * レシピの変換部分を切り出したインターフェースです。
 * @param INPUT_A 1つめのレシピの入力となるクラス
 * @param INPUT_B 2つめのレシピの入力となるクラス
 * @param OUTPUT レシピの出力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun interface HTBiRecipeFactory<INPUT_A : Any, INPUT_B : Any, OUTPUT : Any> {
    /**
     * 指定された[firstInput]と[secondInput]から完成品を作成します。
     */
    fun assemble(firstInput: INPUT_A, secondInput: INPUT_B): OUTPUT
}
