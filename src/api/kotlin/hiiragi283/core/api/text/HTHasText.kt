package hiiragi283.core.api.text

import net.minecraft.network.chat.Component

/**
 * [テキスト][Component]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.text.IHasTextComponent
 */
fun interface HTHasText {
    /**
     * [テキスト][Component]を取得します。
     */
    fun getText(): Component
}
