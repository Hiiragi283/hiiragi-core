package hiiragi283.lib.gui.menu

import hiiragi283.lib.registry.HTDeferredMenuType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

abstract class HTContainerWithContextMenu<out C>(
    menuType: HTDeferredMenuType.WithContext<*, C>,
    containerId: Int,
    inventory: Inventory,
    context: C,
) : HTContainerMenu<@UnsafeVariance C>(
    menuType.get(),
    containerId,
    inventory,
    context,
) {
    init {
        (context as? HTMenuCallback)?.openMenu(inventory.player)
    }

    override fun removed(player: Player) {
        super.removed(player)
        (context as? HTMenuCallback)?.closeMenu(player)
    }
}
