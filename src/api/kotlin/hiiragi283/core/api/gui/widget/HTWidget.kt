package hiiragi283.core.api.gui.widget

import hiiragi283.core.api.gui.HTBounds

/**
 * GUI上の要素を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTWidget {
    /**
     * [HTWidgetType]を取得します。
     */
    fun getType(): HTWidgetType<*>

    /**
     * このウィジェットの[範囲][HTBounds]を取得します。
     */
    fun getBound(): HTBounds

    /**
     * カーソルがこのウィジェットの範囲内にあるか判定します。
     */
    fun isHovered(mouseX: Int, mouseY: Int): Boolean = getBound().contains(mouseX, mouseY)

    /**
     * このウィジェットをクリックした時に呼び出されます。
     */
    fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {}

    /**
     * このウィジェットの上でクリックを解放した時に呼び出されます。
     */
    fun mouseReleased(mouseX: Double, mouseY: Double) {}

    /**
     * このウィジェットをドラッグしている間に呼び出されます。
     */
    fun mouseDragged(
        mouseX: Double,
        mouseY: Double,
        dragX: Double,
        dragY: Double,
    ) {}

    /**
     * このウィジェットをスクロールしている間に呼び出されます。
     */
    fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        scrollX: Double,
        scrollY: Double,
    ): Boolean = false

    /**
     * このウィジェットの上でキーを押すと呼び出されます。
     */
    fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = false

    /**
     * このウィジェットの上でキーを解放した時に呼び出されます。
     */
    fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = false

    /**
     * このウィジェットの上で文字を入力すると呼び出されます。
     */
    fun charTyped(codePoint: Char, modifiers: Int): Boolean = false
}
