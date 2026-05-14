package hiiragi283.lib.gui.widget

import hiiragi283.lib.math.HTBounds

/**
 * [HTWidget]の抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTAbstractWidget(final override val bounds: HTBounds) : HTWidget {
    constructor(x: Int, y: Int, width: Int, height: Int) : this(HTBounds(x, y, width, height))
}
