package hiiragi283.core.api.gui.widget

import hiiragi283.core.api.gui.HTBounds

/**
 * [HTWidget]の抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTAbstractWidget(final override val bounds: HTBounds) : HTWidget {
    constructor(x: Int, y: Int, width: Int, height: Int) : this(HTBounds(x, y, width, height))
}
