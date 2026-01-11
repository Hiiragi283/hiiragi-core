package hiiragi283.core.api.text

import hiiragi283.core.api.HTDefaultColor
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

/**
 * 翻訳を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.text.ILangEntry
 */
interface HTTranslation : HTHasTranslationKey {
    /**
     * [テキスト][Component]を返します。
     */
    fun translate(): MutableComponent = translatableText(this.translationKey)

    /**
     * [テキスト][Component]を返します。
     * @param args テキストの引数
     */
    fun translate(vararg args: Any?): MutableComponent = HTTextUtil.smartTranslate(this.translationKey, *args)

    /**
     * [color]で着色された[テキスト][Component]を返します。
     */
    fun translateColored(color: HTDefaultColor): MutableComponent = translate().withStyle(color)

    /**
     * [color]で着色された[テキスト][Component]を返します。
     * @param args テキストの引数
     */
    fun translateColored(color: HTDefaultColor, vararg args: Any?): MutableComponent = translate(*args).withStyle(color)
}
