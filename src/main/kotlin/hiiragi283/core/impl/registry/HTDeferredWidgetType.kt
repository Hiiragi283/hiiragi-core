package hiiragi283.core.impl.registry

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

class HTDeferredWidgetType<WIDGET : HTWidget> : HTBasicHolderLike<HTWidgetType<*>, HTWidgetType<WIDGET>> {
    constructor(key: ResourceKey<HTWidgetType<*>>) : super(key)

    constructor(id: Identifier) : super(HCRegistries.Keys.WIDGET_TYPE, id)

    @Suppress("UNCHECKED_CAST")
    override fun get(): HTWidgetType<WIDGET> = HCRegistries.WIDGET_TYPE.getOrThrow(key) as HTWidgetType<WIDGET>

    override fun toString(): String = "HTDeferredWidgetType(key=$key)"
}
