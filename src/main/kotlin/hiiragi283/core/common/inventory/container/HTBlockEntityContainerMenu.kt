package hiiragi283.core.common.inventory.container

import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.registry.HTDeferredMenuType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

abstract class HTBlockEntityContainerMenu<BE : HTBlockEntity>(
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
        initSlots()
        // player inventory
        addPlayerInv(inventory)
    }

    protected abstract fun initSlots()

    override fun stillValid(player: Player): Boolean = !context.isRemoved && context.level?.isInWorldBounds(context.blockPos) == true
}
