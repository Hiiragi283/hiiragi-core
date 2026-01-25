package hiiragi283.core.client.gui.tooltip

import hiiragi283.core.api.storage.attachments.HTAttachedFluids
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResourcePair
import hiiragi283.core.common.gui.tooltip.HTFluidFilterTooltip
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTClientFluidFilterTooltip(val fluids: HTAttachedFluids) : ClientTooltipComponent {
    constructor(tooltip: HTFluidFilterTooltip) : this(tooltip.fluids)

    override fun getHeight(): Int = 18

    override fun getWidth(font: Font): Int = 18 * fluids.size

    override fun renderImage(
        font: Font,
        x: Int,
        y: Int,
        guiGraphics: GuiGraphics,
    ) {
        for (i: Int in fluids.indices) {
            val (resource: HTFluidResourceType, amount: Int) = fluids[i].toResourcePair() ?: continue
        }
    }
}
