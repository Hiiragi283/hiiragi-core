package hiiragi283.lib.gui

import net.minecraft.world.item.ItemStack

/**
 * GUIを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTAbstractGui {
    /**
     * カーソル上の[アイテム][ItemStack]を取得します。
     */
    val carried: ItemStack get() = ItemStack.EMPTY

    /**
     * このGUIの始点のx座標を取得します。
     */
    fun getGuiLeft(): Int

    /**
     * このGUIの始点のy座標を取得します。
     */
    fun getGuiTop(): Int

    /**
     * このGUIの幅を取得します。
     */
    fun getXSize(): Int

    /**
     * このGUIの高さを取得します。
     */
    fun getYSize(): Int
}
