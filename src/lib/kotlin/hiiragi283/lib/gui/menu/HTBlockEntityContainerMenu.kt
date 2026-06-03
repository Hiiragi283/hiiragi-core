package hiiragi283.lib.gui.menu

import hiiragi283.lib.block.entity.HTBlockEntity
import hiiragi283.lib.registry.HTDeferredMenuType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

open class HTBlockEntityContainerMenu<BE : HTBlockEntity>(
    menuType: HTDeferredMenuType.WithContext<*, BE>,
    containerId: Int,
    inventory: Inventory,
    context: BE,
) : HTContainerWithContextMenu<BE>(
    menuType,
    containerId,
    inventory,
    context,
) {
    init {
        // tracking slots
        context.addMenuTrackers(this)
        // block entity slots
        // addSlots(context)
        // player inventory
        addPlayerInv(inventory, 0)
    }

    override fun stillValid(player: Player): Boolean = !context.isRemoved && context.level?.isInWorldBounds(context.blockPos) == true
}
