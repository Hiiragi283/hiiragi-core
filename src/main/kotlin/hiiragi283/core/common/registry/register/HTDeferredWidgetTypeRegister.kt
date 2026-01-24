package hiiragi283.core.common.registry.register

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.common.registry.HTDeferredWidgetType
import net.minecraft.resources.ResourceLocation

class HTDeferredWidgetTypeRegister(namespace: String) : HTDeferredRegister<HTWidgetType<*>>(HCRegistries.Keys.WIDGET_TYPE, namespace) {
    fun <WIDGET : HTWidget<WIDGET>> registerType(name: String): HTDeferredWidgetType<WIDGET> {
        val recipeType = HTDeferredWidgetType<WIDGET>(createId(name))
        register(name) { id: ResourceLocation -> HTWidgetType.Simple<WIDGET>(id) }
        return recipeType
    }
}
