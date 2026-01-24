package hiiragi283.core.client.gui.screen

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.common.gui.menu.HTContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
abstract class HTContainerScreen<MENU : HTContainerMenu<*>>(menu: MENU, inventory: Inventory, title: Component) :
    AbstractContainerScreen<MENU>(menu, inventory, title) {
    override fun render(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        updateVisibility()
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    protected open fun updateVisibility() {}

    fun getBounds(): HTBounds = HTBounds(this.leftPos, this.topPos, this.imageWidth, this.imageHeight)

    //    Extensions    //

    val startX: Int get() = (width - imageWidth) / 2

    val startY: Int get() = (height - imageHeight) / 2

    protected inline fun <WIDGET : AbstractWidget> addWidget(x: Int, y: Int, factory: (Int, Int) -> WIDGET): WIDGET =
        factory(startX + x, startY + y).let(::addRenderableWidget)
}
