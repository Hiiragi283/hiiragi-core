package hiiragi283.lib.text

/**
 * [テキスト][Text]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.text.IHasTextComponent
 */
fun interface HTHasText {
    /**
     * [テキスト][Text]を取得します。
     */
    fun getText(): Text
}
