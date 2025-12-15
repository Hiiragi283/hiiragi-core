package hiiragi283.core.api.text

import net.minecraft.network.chat.Component

/**
 * [Component]を保持するインターフェース
 * @see mekanism.api.text.IHasTextComponent
 */
fun interface HTHasText {
    fun getText(): Component
}
