package hiiragi283.core.api.gui.widget

import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * [HTWidgetRenderer]の抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@OnlyIn(Dist.CLIENT)
abstract class HTAbstractWidgetRenderer<WIDGET : HTWidget>(protected val widget: WIDGET) : HTWidgetRenderer<WIDGET>
