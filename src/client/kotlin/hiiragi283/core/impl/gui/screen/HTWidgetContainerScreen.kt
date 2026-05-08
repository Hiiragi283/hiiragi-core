package hiiragi283.core.impl.gui.screen

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.Text
import hiiragi283.core.common.gui.menu.HTWidgetContainerMenu
import hiiragi283.core.impl.gui.widget.HTGuiWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @see net.minecraft.client.gui.screens.inventory.ContainerScreen
 */
@OnlyIn(Dist.CLIENT)
class HTWidgetContainerScreen(menu: HTWidgetContainerMenu, inventory: Inventory, title: Text) : HTContainerScreen<HTWidgetContainerMenu>(menu, inventory, title) {
    companion object {
        @JvmField
        val BACKGROUND: ResourceLocation = HiiragiCoreAPI.id(HTConst.TEXTURES, HTConst.GUI, "background.png")
    }

    private val rows: Int = menu.widgetHolder.rows

    init {
        imageHeight = 144 + rows * 18
        inventoryLabelY = imageHeight - 125
    }

    override fun init() {
        super.init()
        titleLabelX = (imageWidth - font.width(title)) / 2
        menu.widgetHolder.map { HTGuiWidget(this, it) }.forEach(::addRenderableWidget)
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        val slotHeight: Int = 18 * rows + 17
        guiGraphics.blit(BACKGROUND, startX, startY, 0, 0, imageWidth, slotHeight)
        guiGraphics.blit(BACKGROUND, startX, startY + slotHeight, 0, 126, imageWidth, 96)
    }
}
