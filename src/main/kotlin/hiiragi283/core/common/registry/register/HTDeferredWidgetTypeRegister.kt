package hiiragi283.core.common.registry.register

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.registry.HTDeferredRegisterN
import hiiragi283.core.common.registry.HTDeferredWidgetType
import net.minecraft.resources.ResourceLocation

class HTDeferredWidgetTypeRegister(namespace: String) : HTDeferredRegisterN<HTWidgetType<*>>(HCRegistries.Keys.WIDGET_TYPE, namespace) {
    fun <WIDGET : HTWidget> registerType(name: String): HTDeferredWidgetType<WIDGET> {
        val recipeType = HTDeferredWidgetType<WIDGET>(createId(name))
        delegate.register(name) { id: ResourceLocation -> HTWidgetType.Simple<WIDGET>(id) }
        return recipeType
    }
}
