package hiiragi283.core.api.gui.widget

import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
abstract class HTAbstractWidgetRenderer<WIDGET : HTWidget>(protected val widget: WIDGET) : HTWidgetRenderer<WIDGET>
