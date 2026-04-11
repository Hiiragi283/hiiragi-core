package hiiragi283.core.api.event

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetRendererFactory
import hiiragi283.core.api.gui.widget.HTWidgetType
import net.minecraft.client.gui.components.Renderable
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent

/**
 * [HTWidgetType]と[Renderable]を紐づけるイベントクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
 */
@OnlyIn(Dist.CLIENT)
class HTRegisterWidgetRendererEvent(
    private val registerer: (HTWidgetType<*>, HTWidgetRendererFactory<*, *>) -> HTWidgetRendererFactory<*, *>?,
) : Event(),
    IModBusEvent {
    /**
     * 指定した[type]と[factory]を紐づけます。
     * @param WIDGET [HTWidget]を実装したクラス
     * @throws IllegalStateException すでに[type]に[HTWidgetRendererFactory]が登録されている場合
     */
    fun <WIDGET : HTWidget, RENDERER : Renderable> register(
        type: HTWidgetType<WIDGET>,
        factory: HTWidgetRendererFactory<WIDGET, RENDERER>,
    ) {
        check(registerer(type, factory) == null) {
            "Duplicated widget renderer for ${
                HCRegistries.WIDGET_TYPE.getKey(
                    type,
                )
            }"
        }
    }
}
