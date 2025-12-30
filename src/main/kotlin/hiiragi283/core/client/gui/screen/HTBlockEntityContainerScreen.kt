package hiiragi283.core.client.gui.screen

import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

open class HTBlockEntityContainerScreen<BE : HTBlockEntity, MENU : HTBlockEntityContainerMenu<BE>> : HTContainerScreen<MENU> {
    companion object {
        @JvmStatic
        fun <MENU : HTBlockEntityContainerMenu<*>> getMenuTexture(menu: MENU): ResourceLocation = menu.context
            .getDeferredType()
            .getId()
            .withPath { "textures/gui/container/$it.png" }
    }

    constructor(
        texture: ResourceLocation,
        menu: MENU,
        inventory: Inventory,
        title: Component,
    ) : super(
        texture,
        menu,
        inventory,
        title,
    )

    constructor(menu: MENU, inventory: Inventory, title: Component) : super(
        getMenuTexture(menu),
        menu,
        inventory,
        title,
    )

    val blockEntity: BE get() = menu.context
}
