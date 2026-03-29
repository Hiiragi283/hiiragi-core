package hiiragi283.core.api.gui.widget

import hiiragi283.core.api.gui.HTBounds
import net.minecraft.world.item.ItemStack

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
     * このウィジェット自体の[範囲][HTBounds]
     *
     * GUI上での範囲ではありません
     */
    val bounds: HTBounds

    fun setupHolder(widgetHolder: HTWidgetHolder) {}

    /**
     * @since 0.12.0
     */
    fun onInit(access: Access) {}

    /**
     * このウィジェットをクリックした時に呼び出されます。
     */
    fun mouseClicked(
        access: Access,
        mouseX: Double,
        mouseY: Double,
        button: Int,
    ) {}

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

    /**
     * クライアント側のウィジェットへのアクセスを表すインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Access {
        var isActive: Boolean
        var isVisible: Boolean
        val carried: ItemStack
    }
}
