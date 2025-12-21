package hiiragi283.core.api.text

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

/**
 * 翻訳を保持するインターフェース
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
    fun translateColored(color: ChatFormatting): MutableComponent = translate().withStyle(color)

    /**
     * [color]で着色された[テキスト][Component]を返します。
     * @param args テキストの引数
     */
    fun translateColored(color: ChatFormatting, vararg args: Any?): MutableComponent = translate(*args).withStyle(color)
}
