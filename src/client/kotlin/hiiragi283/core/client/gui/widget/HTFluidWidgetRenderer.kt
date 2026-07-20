package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.HTGuiAccess
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.fluid.getStillTexture
import hiiragi283.core.api.storage.fluid.getTintColor
import hiiragi283.core.api.text.Text
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.util.HTSpriteRenderHelper
import hiiragi283.core.util.HTStorageHelper
import java.util.function.Consumer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction

@OnlyIn(Dist.CLIENT)
class HTFluidWidgetRenderer(gui: HTGuiAccess, widget: HTFluidWidget) : HTSpriteWidgetRenderer<HTFluidWidget>(gui, widget) {
    override fun renderBackground(bounds: HTBounds, guiGraphics: GuiGraphics) {
        val texture: ResourceLocation = when (widget) {
            is HTFluidWidget.Slot -> widget.backgroundType.slotTexture
            is HTFluidWidget.Tank -> widget.backgroundType.tankTexture
        }
        HTSpriteRenderHelper.blit(guiGraphics, texture, bounds)
    }

    override fun shouldRender(): Boolean = widget.getResource() != null

    override fun getSprite(): TextureAtlasSprite? = getSprite(widget.getResource()?.getStillTexture())

    override fun getColor(): Int = widget.getResource()?.getTintColor() ?: -1

    override fun getLevel(): Fraction = when (widget) {
        is HTFluidWidget.Slot -> Fraction.ONE
        is HTFluidWidget.Tank -> widget.getLevelAsFraction()
    }.coerceAtMost(Fraction.ONE)

    override fun collectTooltips(consumer: Consumer<Text>, flag: TooltipFlag) {
        HTStorageHelper.addFluidTooltip(widget.getFluidStack(), consumer, flag, false)
    }
}
