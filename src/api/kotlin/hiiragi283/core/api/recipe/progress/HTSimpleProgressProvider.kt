package hiiragi283.core.api.recipe.progress

/**
 * 常に一定の時間やエネルギーで処理するレシピに付与するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
interface HTSimpleProgressProvider {
    val progressData: HTProgressData
}
