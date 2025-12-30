package hiiragi283.core.client.emi

import dev.emi.emi.api.EmiStackProvider
import dev.emi.emi.api.stack.EmiStackInteraction
import hiiragi283.core.api.gui.component.HTFluidWidget
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.math.HTBounds
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.client.gui.screen.HTContainerScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.world.inventory.Slot

object HCEmiStackProvider : EmiStackProvider<Screen> {
    override fun getStackAt(screen: Screen, x: Int, y: Int): EmiStackInteraction {
        if (screen is HTContainerScreen<*>) {
            // Get stack from slots
            for (slot: Slot in screen.menu.slots) {
                if (HTBounds.createSlot(screen.startX + slot.x, screen.startY + slot.y).contains(x, y)) {
                    return EmiStackInteraction(slot.item.toEmi(), null, false)
                }
            }
            // Get stack from tanks
            for (widget: HTFluidWidget in screen.fluidWidgets) {
                if (widget.isHovered(x, y)) {
                    return EmiStackInteraction(widget.getFluidStack().toEmi(), null, false)
                }
            }
        }

        return EmiStackInteraction.EMPTY
    }
}
