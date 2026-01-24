package hiiragi283.core.common.gui.menu

import hiiragi283.core.api.gui.menu.HTItemContainerContext
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.registry.HTDeferredMenuType
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.Optional

abstract class HTItemContainerMenu(
    menuType: HTDeferredMenuType<*, *>,
    containerId: Int,
    inventory: Inventory,
    context: HTItemContainerContext,
) : HTContainerMenu<HTItemContainerContext>(menuType, containerId, inventory, context) {
    protected val hand: Optional<InteractionHand> = context.hand
    protected val stack: ItemStack = context.stack

    final override fun stillValid(player: Player): Boolean = hand
        .map { interactionHand: InteractionHand ->
            stack.`is`(player.getItemInHand(interactionHand).item)
        }.orElseGet {
            stack.`is`(HiiragiCoreTags.Items.BYPASS_MENU_VALIDATION)
        }
}
