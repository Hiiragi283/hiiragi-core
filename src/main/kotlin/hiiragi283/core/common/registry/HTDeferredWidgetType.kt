package hiiragi283.core.common.registry

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.createKey
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

class HTDeferredWidgetType<WIDGET : HTWidget<WIDGET>>(private val key: ResourceKey<HTWidgetType<*>>) :
    HTHolderLike<HTWidgetType<*>, HTWidgetType<WIDGET>> {
    constructor(id: ResourceLocation) : this(HCRegistries.Keys.WIDGET_TYPE.createKey(id))

    override fun getResourceKey(): ResourceKey<HTWidgetType<*>> = key

    @Suppress("UNCHECKED_CAST")
    override fun get(): HTWidgetType<WIDGET> = HCRegistries.WIDGET_TYPE.getOrThrow(key) as HTWidgetType<WIDGET>
}
