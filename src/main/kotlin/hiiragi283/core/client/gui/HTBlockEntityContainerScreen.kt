package hiiragi283.core.client.gui

import hiiragi283.core.api.gui.component.HTFluidWidget
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

open class HTBlockEntityContainerScreen<BE : HTBlockEntity> : HTContainerScreen<HTBlockEntityContainerMenu<BE>> {
    companion object {
        @JvmStatic
        fun <BE : HTBlockEntity> getMenuTexture(menu: HTBlockEntityContainerMenu<BE>): ResourceLocation = menu.context
            .getDeferredType()
            .getId()
            .withPath { "textures/gui/container/$it.png" }
    }

    constructor(
        texture: ResourceLocation,
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : super(
        texture,
        menu,
        inventory,
        title,
    )

    constructor(menu: HTBlockEntityContainerMenu<BE>, inventory: Inventory, title: Component) : super(
        getMenuTexture(menu),
        menu,
        inventory,
        title,
    )

    val blockEntity: BE get() = menu.context

    //    Extensions    //

    private val fluidWidgets: MutableList<HTFluidWidget> = mutableListOf()

    protected fun <WIDGET> addFluidWidget(widget: WIDGET): WIDGET where WIDGET : AbstractWidget, WIDGET : HTFluidWidget {
        fluidWidgets.add(widget)
        addRenderableWidget(widget)
        return widget
    }

    override fun clearWidgets() {
        super.clearWidgets()
        fluidWidgets.clear()
    }

    final override fun getFluidWidgets(): List<HTFluidWidget> = fluidWidgets
}
