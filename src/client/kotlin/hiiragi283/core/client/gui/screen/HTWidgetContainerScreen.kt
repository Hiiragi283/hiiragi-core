package hiiragi283.core.client.gui.screen

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.client.gui.widget.HTGuiWidget
import hiiragi283.core.common.gui.menu.HTWidgetContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTWidgetContainerScreen(menu: HTWidgetContainerMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTWidgetContainerMenu>(menu, inventory, title) {
    companion object {
        @JvmField
        val BACKGROUND: ResourceLocation = HiiragiCoreAPI.id(HTConst.TEXTURES, HTConst.GUI, "background.png")
    }

    init {
        imageHeight = 144 + menu.widgetHolder.rows * 18

        inventoryLabelY = imageHeight - 94
    }

    val widgetHolder: HTWidgetHolder get() = menu.widgetHolder

    override fun init() {
        super.init()
        titleLabelX = (imageWidth - font.width(title)) / 2
        widgetHolder.map { HTGuiWidget(this, it) }.forEach(::addRenderableWidget)
    }

    /**
     * @see net.minecraft.client.gui.screens.inventory.ContainerScreen
     */
    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        val widgetsHeight: Int = HTSlotHelper.getSlotPosY(widgetHolder.rows)
        guiGraphics.blit(BACKGROUND, startX, startY, 0, 0, imageWidth, widgetsHeight)
        guiGraphics.blit(
            BACKGROUND,
            startX,
            startY + widgetsHeight,
            0,
            HTSlotHelper.getSlotPosY(6) + 2,
            imageWidth,
            HTSlotHelper.getSlotPosY(4) + 9,
        )
    }
}
