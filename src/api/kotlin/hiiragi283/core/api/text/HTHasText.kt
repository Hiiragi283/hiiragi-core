package hiiragi283.core.api.text

import net.minecraft.network.chat.Component

/**
 * [テキスト][Component]を保持するインターフェース
 * @see mekanism.api.text.IHasTextComponent
 */
fun interface HTHasText {
    /**
     * [テキスト][Component]を取得します。
     */
    fun getText(): Component
}
