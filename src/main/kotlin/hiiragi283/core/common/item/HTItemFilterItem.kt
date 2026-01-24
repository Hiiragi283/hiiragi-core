package hiiragi283.core.common.item

import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.common.capability.HTItemCapabilities
import hiiragi283.core.common.gui.factory.HTItemWidgetHolderContext
import hiiragi283.core.common.storage.item.HTComponentItemSlot
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTItemFilterItem(properties: Properties) :
    Item(properties),
    HTItemWidgetHolderContext.Factory {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        if (player is ServerPlayer) {
            HTItemWidgetHolderContext.openMenu(player, usedHand)
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide)
    }

    override fun setup(context: HTItemWidgetHolderContext, widgetHolder: HTWidgetHolder) {
        HTItemCapabilities
            .getItemSlots(context.stack)
            .filterIsInstance<HTComponentItemSlot>()
    }
}
