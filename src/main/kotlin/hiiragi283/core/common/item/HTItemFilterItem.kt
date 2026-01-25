package hiiragi283.core.common.item

import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.common.capability.HTItemCapabilities
import hiiragi283.core.common.gui.factory.HTItemWidgetHolderContext
import hiiragi283.core.common.gui.tooltip.HTItemFilterTooltip
import hiiragi283.core.common.gui.widget.HTItemStackWidget
import hiiragi283.core.common.storage.item.HTComponentItemSlot
import hiiragi283.core.setup.HCDataComponents
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.*

class HTItemFilterItem(properties: Properties) :
    Item(properties),
    HTItemWidgetHolderContext.Factory {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        if (player is ServerPlayer) {
            HTItemWidgetHolderContext.openMenu(player, usedHand)
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide)
    }

    override fun getTooltipImage(stack: ItemStack): Optional<TooltipComponent> = stack
        .get(HCDataComponents.ITEM)
        ?.let(::HTItemFilterTooltip)
        ?.takeUnless { stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP) }
        .let { Optional.ofNullable(it) }

    override fun setup(context: HTItemWidgetHolderContext, widgetHolder: HTWidgetHolder) {
        HTItemCapabilities
            .getItemSlots(context.stack)
            .filterIsInstance<HTComponentItemSlot>()
            .mapIndexed { index: Int, slot: HTComponentItemSlot ->
                HTItemStackWidget(
                    slot,
                    slot::setStack,
                    HTSlotHelper.getSlotPosX(index),
                    HTSlotHelper.getSlotPosY(1),
                )
            }.forEach(widgetHolder::addWidget)
    }
}
