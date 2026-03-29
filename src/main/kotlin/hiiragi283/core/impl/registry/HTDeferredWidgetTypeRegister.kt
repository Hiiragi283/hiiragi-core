package hiiragi283.core.impl.registry

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.registry.HTDeferredRegister
import net.minecraft.resources.Identifier

class HTDeferredWidgetTypeRegister(namespace: String) : HTDeferredRegister<HTWidgetType<*>>(HCRegistries.Keys.WIDGET_TYPE, namespace) {
    fun <WIDGET : HTWidget> registerType(name: String): HTDeferredWidgetType<WIDGET> {
        val recipeType = HTDeferredWidgetType<WIDGET>(createId(name))
        delegate.register(name) { id: Identifier -> HTWidgetType.Simple<WIDGET>(id) }
        return recipeType
    }
}
