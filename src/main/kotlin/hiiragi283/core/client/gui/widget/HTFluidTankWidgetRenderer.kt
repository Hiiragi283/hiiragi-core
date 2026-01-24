package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.fluid.getStillTexture
import hiiragi283.core.api.storage.fluid.getTintColor
import hiiragi283.core.common.gui.widget.HTFluidTankWidget
import hiiragi283.core.util.HTTooltipHelper
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

@OnlyIn(Dist.CLIENT)
class HTFluidTankWidgetRenderer(widget: HTFluidTankWidget) : HTSpriteWidgetRenderer<HTFluidTankWidget>(widget) {
    override fun shouldRender(): Boolean = widget.getResource() != null

    override fun getSprite(): TextureAtlasSprite? = getSprite(widget.getResource()?.getStillTexture())

    override fun getColor(): Int = widget.getResource()?.getTintColor() ?: -1

    override fun getLevel(): Fraction = widget.getLevelAsFraction()

    override fun collectTooltips(consumer: Consumer<Component>, flag: TooltipFlag) {
        HTTooltipHelper.addFluidTooltip(widget.getFluidStack(), consumer, flag, false)
    }
}
