package hiiragi283.core.common.gui.menu

import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.registry.HTDeferredMenuType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

abstract class HTBlockContainerMenu<BE : HTBlockEntity>(
    menuType: HTDeferredMenuType<*, *>,
    containerId: Int,
    inventory: Inventory,
    context: BE,
) : HTContainerMenu<BE>(menuType, containerId, inventory, context) {
    final override fun stillValid(player: Player): Boolean = context.isRemoved && context.level?.isInWorldBounds(context.blockPos) == true
}
