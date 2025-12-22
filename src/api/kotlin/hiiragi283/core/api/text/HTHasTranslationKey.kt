package hiiragi283.core.api.text

/**
 * 翻訳キーを保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.text.IHasTranslationKey
 */
interface HTHasTranslationKey {
    /**
     * 翻訳キーの値
     */
    val translationKey: String
}
