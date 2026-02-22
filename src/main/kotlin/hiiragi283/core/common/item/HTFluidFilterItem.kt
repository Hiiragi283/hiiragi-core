package hiiragi283.core.common.item

import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.storage.attachments.HTAttachedFluids
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.common.gui.factory.HTItemWidgetHolderContext
import hiiragi283.core.common.gui.sync.HTFluidSyncSlot
import hiiragi283.core.common.gui.tooltip.HTFluidFilterTooltip
import hiiragi283.core.common.gui.widget.HTFluidWidget
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
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

class HTFluidFilterItem(properties: Properties) :
    Item(properties),
    HTItemWidgetHolderContext.Factory {
    companion object {
        const val MAX_SLOTS = 9

        @JvmStatic
        private fun getOrCreateAttached(stack: ItemStack): HTAttachedFluids = HTCapabilityCodec.FLUID.getOrCreate(stack, MAX_SLOTS)
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        if (player is ServerPlayer) {
            HTItemWidgetHolderContext.openMenu(player, usedHand)
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide)
    }

    override fun getTooltipImage(stack: ItemStack): Optional<TooltipComponent> = getOrCreateAttached(stack)
        .let(::HTFluidFilterTooltip)
        .takeUnless { stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP) }
        .let { Optional.ofNullable(it) }

    override fun setup(context: HTItemWidgetHolderContext, widgetHolder: HTWidgetHolder) {
        widgetHolder.rows = 1

        val stack: ItemStack = context.stack
        (0 until MAX_SLOTS)
            .map { index: Int ->
                HTFluidSyncSlot(
                    { getOrCreateAttached(stack)[index] },
                    { stackIn: FluidStack ->
                        val newAttached: HTAttachedFluids = getOrCreateAttached(stack).with(index, stackIn)
                        HTCapabilityCodec.FLUID.updateAttached(stack, newAttached)
                    },
                )
            }.mapIndexed { index: Int, slot: HTFluidSyncSlot ->
                HTFluidWidget
                    .createSlot(
                        object : HTFluidView {
                            override fun getResource(): HTFluidResourceType? = slot.asFluidStack.toResource()

                            override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

                            override fun getAmount(): Int = slot.amountAsInt
                        },
                        slot,
                        HTSlotHelper.getSlotPosX(index),
                        HTSlotHelper.getSlotPosY(0),
                    ).setGhost()
            }.forEach(widgetHolder::addWidget)
    }
}
