package hiiragi283.core.api.recipe.progress

/**
 * レシピの処理に必要な時間またはエネルギーを提供するインターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
interface HTProgressProvider<INPUT : Any> {
    fun getProgressData(input: INPUT): HTProgressData

    /**
     * @author Hiiragi Tsubasa
     * @since 21.1.1.0
     */
    interface Simple<INPUT_A : Any> :
        HTProgressProvider<INPUT_A>,
        HTSimpleProgressProvider {
        override fun getProgressData(input: INPUT_A): HTProgressData = progressData
    }
}
