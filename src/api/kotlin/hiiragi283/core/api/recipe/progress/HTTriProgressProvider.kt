package hiiragi283.core.api.recipe.progress

/**
 * レシピの処理に必要な時間またはエネルギーを提供するインターフェースです。
 * @param INPUT_A 1つめのレシピの入力となるクラス
 * @param INPUT_B 2つめのレシピの入力となるクラス
 * @param INPUT_C 3つめのレシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
interface HTTriProgressProvider<INPUT_A : Any, INPUT_B : Any, INPUT_C : Any> {
    fun getProgressData(firstInput: INPUT_A, secondInput: INPUT_B, thirdInput: INPUT_C): HTProgressData

    /**
     * @author Hiiragi Tsubasa
     * @since 21.1.1.0
     */
    interface Simple<INPUT_A : Any, INPUT_B : Any, INPUT_C : Any> :
        HTTriProgressProvider<INPUT_A, INPUT_B, INPUT_C>,
        HTSimpleProgressProvider {
        override fun getProgressData(firstInput: INPUT_A, secondInput: INPUT_B, thirdInput: INPUT_C): HTProgressData = progressData
    }
}
