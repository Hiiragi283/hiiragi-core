package hiiragi283.core.client.gui.screen

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.client.gui.widget.HTGuiWidget
import hiiragi283.core.common.gui.menu.HTWidgetContainerMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory

class HTWidgetContainerScreen(
    menu: HTWidgetContainerMenu,
    inventory: Inventory,
    title: Component,
    private val rows: Int,
) : HTContainerScreen<HTWidgetContainerMenu>(menu, inventory, title, 176, 144 + rows * 18) {
    companion object {
        @JvmField
        val BACKGROUND: Identifier = HiiragiCoreAPI.id(HTConst.TEXTURES, HTConst.GUI, "background.png")
    }

    constructor(
        menu: HTWidgetContainerMenu,
        inventory: Inventory,
        title: Component,
    ) : this(menu, inventory, title, menu.widgetHolder.rows)

    init {
        inventoryLabelY = imageHeight - 125
    }

    override fun init() {
        super.init()
        titleLabelX = (imageWidth - font.width(title)) / 2
        menu.widgetHolder.map { HTGuiWidget(this, it) }.forEach(::addRenderableWidget)
    }

    override fun extractBackground(
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        a: Float,
    ) {
        val slotHeight: Int = 18 * rows + 17
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, startX, startY, 0f, 0f, imageWidth, slotHeight, 256, 156)
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, startX, startY + slotHeight, 0f, 126f, imageWidth, 96, 256, 256)
    }
}
