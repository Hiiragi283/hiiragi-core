package hiiragi283.core.client.gui

import hiiragi283.core.api.gui.component.HTFluidWidget
import hiiragi283.core.api.math.HTBounds
import hiiragi283.core.api.math.HTBoundsProvider
import hiiragi283.core.common.inventory.container.HTContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
abstract class HTContainerScreen<MENU : HTContainerMenu>(
    val texture: ResourceLocation?,
    menu: MENU,
    inventory: Inventory,
    title: Component,
) : AbstractContainerScreen<MENU>(menu, inventory, title),
    HTBoundsProvider {
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

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        texture?.let {
            guiGraphics.blit(it, startX, startY, 0, 0, imageWidth, imageHeight)
        }
    }

    open fun getFluidWidgets(): List<HTFluidWidget> = listOf()

    //    HTBoundsProvider    //

    final override fun getBounds(): HTBounds = HTBounds(this.leftPos, this.topPos, this.imageWidth, this.imageHeight)

    //    Extensions    //

    val startX: Int get() = (width - imageWidth) / 2

    val startY: Int get() = (height - imageHeight) / 2
}
