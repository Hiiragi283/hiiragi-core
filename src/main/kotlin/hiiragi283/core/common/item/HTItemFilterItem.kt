package hiiragi283.core.common.item

import hiiragi283.core.api.function.wrapOptional
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.common.gui.factory.HTItemWidgetHolderContext
import hiiragi283.core.common.gui.sync.HTItemSyncSlot
import hiiragi283.core.common.gui.tooltip.HTItemFilterTooltip
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.storage.HTCapabilityCodec
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.Optional

class HTItemFilterItem(properties: Properties) :
    Item(properties),
    HTItemWidgetHolderContext.Factory {
    companion object {
        const val MAX_SLOTS = 9

        @JvmStatic
        private fun getOrCreateAttached(stack: ItemStack): HTAttachedItems = HTCapabilityCodec.ITEM.getOrCreate(stack, MAX_SLOTS)
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        if (player is ServerPlayer) {
            HTItemWidgetHolderContext.openMenu(player, usedHand)
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide)
    }

    override fun getTooltipImage(stack: ItemStack): Optional<TooltipComponent> = getOrCreateAttached(stack)
        .let(::HTItemFilterTooltip)
        .takeUnless { it.items.isEmpty() || stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP) }
        .wrapOptional()

    override fun setup(context: HTItemWidgetHolderContext, widgetHolder: HTWidgetHolder) {
        widgetHolder.rows = 1

        val stack: ItemStack = context.stack
        (0 until MAX_SLOTS)
            .map { index: Int ->
                HTItemSyncSlot(
                    { getOrCreateAttached(stack)[index] },
                    { stackIn: ItemStack ->
                        val newAttached: HTAttachedItems = getOrCreateAttached(stack).with(index, stackIn)
                        HTCapabilityCodec.ITEM.updateAttached(stack, newAttached)
                    },
                )
            }.mapIndexed { index: Int, slot: HTItemSyncSlot ->
                HTItemSlotWidget(
                    slot,
                    HTSlotHelper.getSlotPosX(index),
                    HTSlotHelper.getSlotPosY(0),
                    HTBackgroundType.NONE,
                ).setGhost()
            }.forEach(widgetHolder::addWidget)
    }
}
