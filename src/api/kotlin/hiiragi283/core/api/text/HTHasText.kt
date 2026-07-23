package hiiragi283.core.api.text

/**
 * [テキスト][Text]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.text.IHasTextComponent
 */
interface HTHasText {
    /**
     * [テキスト][Text]を取得します。
     */
    fun getText(): Text
}

fun HTHasText(text: Text): HTHasText = SimpleHasText(text)

@JvmRecord
private data class SimpleHasText(private val text: Text) : HTHasText {
    override fun getText(): Text = text
}
