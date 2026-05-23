package hiiragi283.core.api.registry

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.resources.ResourceLocation

class HTDeferredWidgetTypeRegister(namespace: String) : HTDeferredRegister<HTWidgetType<*>>(HCRegistries.Keys.WIDGET_TYPE, namespace) {
    fun <WIDGET : HTWidget> registerType(name: String): SupplierWithId<HTWidgetType<WIDGET>> = this.register(name) { id: ResourceLocation -> HTWidgetType.Simple(id) }
}
