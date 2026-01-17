package hiiragi283.core.common.item

import com.lowdragmc.lowdraglib2.gui.factory.HeldItemUIMenuType
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import hiiragi283.core.api.gui.HTModularUIHelper
import hiiragi283.core.api.gui.element.HTItemSlotElement
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.capability.HTItemCapabilities
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
    HeldItemUIMenuType.HeldItemUI {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        if (player is ServerPlayer) {
            HeldItemUIMenuType.openUI(player, usedHand)
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide)
    }

    override fun createUI(holder: HeldItemUIMenuType.HeldItemUIHolder): ModularUI = HTModularUIHelper.createVanillaUI(
        holder.player,
        holder.itemStack.hoverName,
    ) {
        this.addRowChild {
            HTItemCapabilities
                .getItemSlots(holder.itemStack)
                .filterIsInstance<HTComponentItemSlot>()
                .map { HTItemSlotElement().xeiPhantom().bind(DataBindingBuilder.itemStack(it::getItemStack, it::setStack).build()) }
                .forEach(this@addRowChild::addChild)
        }
    }
}
