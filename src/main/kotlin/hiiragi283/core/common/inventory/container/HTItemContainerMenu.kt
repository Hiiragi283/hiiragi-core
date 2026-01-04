package hiiragi283.core.common.inventory.container

import hiiragi283.core.api.inventory.container.HTItemContainerContext
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.registry.HTDeferredMenuType
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.Optional

/**
 * [InteractionHand]と[ItemStack]を受け取る[HTContainerMenu]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see mekanism.common.inventory.container.item.MekanismItemContainer
 */
abstract class HTItemContainerMenu(
    menuType: HTDeferredMenuType.OnHand<*>,
    containerId: Int,
    inventory: Inventory,
    context: HTItemContainerContext,
) : HTContainerMenu(
        menuType,
        containerId,
        inventory,
    ) {
    protected val hand: Optional<InteractionHand> = context.hand
    protected val stack: ItemStack = context.stack

    override fun stillValid(player: Player): Boolean = hand
        .map { interactionHand: InteractionHand ->
            stack.`is`(player.getItemInHand(interactionHand).item)
        }.orElseGet {
            stack.`is`(HiiragiCoreTags.Items.BYPASS_MENU_VALIDATION)
        }
}
