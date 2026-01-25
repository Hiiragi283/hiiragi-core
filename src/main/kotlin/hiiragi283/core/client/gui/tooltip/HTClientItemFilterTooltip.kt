package hiiragi283.core.client.gui.tooltip

import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.common.gui.tooltip.HTItemFilterTooltip
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTClientItemFilterTooltip(val items: HTAttachedItems) : ClientTooltipComponent {
    constructor(tooltip: HTItemFilterTooltip) : this(tooltip.items)

    override fun getHeight(): Int = 18

    override fun getWidth(font: Font): Int = 18 * items.size

    override fun renderImage(
        font: Font,
        x: Int,
        y: Int,
        guiGraphics: GuiGraphics,
    ) {
        for (i: Int in items.indices) {
            val stack: ItemStack = items[i]
            guiGraphics.renderFakeItem(stack, x + 18 * i, y)
            guiGraphics.renderItemDecorations(font, stack, x * 18 * i, y)
        }
    }
}
