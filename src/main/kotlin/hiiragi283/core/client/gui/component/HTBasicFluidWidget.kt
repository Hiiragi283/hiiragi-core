package hiiragi283.core.client.gui.component

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.gui.component.HTFluidWidget
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.fluid.getStillTexture
import hiiragi283.core.api.storage.fluid.getTintColor
import hiiragi283.core.util.HTTooltipHelper
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

@OnlyIn(Dist.CLIENT)
abstract class HTBasicFluidWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTSpriteWidget(x, y, width, height, Component.empty()),
    HTFluidWidget {
    final override fun shouldRender(): Boolean = getResource() != null

    final override fun getSprite(): TextureAtlasSprite? = getSprite(getResource()?.getStillTexture(), HTConst.BLOCK_ATLAS)

    final override fun getColor(): Int = getResource()?.getTintColor() ?: -1

    override fun getLevel(): Fraction = getStoredLevel()

    override fun collectTooltips(consumer: Consumer<Component>, flag: TooltipFlag) {
        HTTooltipHelper.addFluidTooltip(getFluidStack(), consumer, flag, false)
    }
}
