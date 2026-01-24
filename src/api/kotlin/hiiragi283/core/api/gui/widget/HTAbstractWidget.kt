package hiiragi283.core.api.gui.widget

import hiiragi283.core.api.gui.HTBounds

/**
 * [HTWidget]の抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTAbstractWidget(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
) : HTWidget {
    final override fun getBound(): HTBounds = HTBounds(x, y, width, height)
}
