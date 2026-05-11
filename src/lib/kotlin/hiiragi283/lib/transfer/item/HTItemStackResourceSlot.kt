package hiiragi283.lib.transfer.item

import hiiragi283.lib.transfer.HTStackResourceSlot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * アイテム向けの[HTStackResourceSlot]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
abstract class HTItemStackResourceSlot : HTStackResourceSlot<ItemResource, ItemStack>() {
    override fun getResourceFrom(stack: ItemStack): ItemResource = ItemResource.of(stack)

    override fun getAmountFrom(stack: ItemStack): Int = stack.count

    override fun isSame(stack: ItemStack, resource: ItemResource): Boolean = getResourceFrom(stack) == resource

    override fun createStack(resource: ItemResource, amount: Int): ItemStack = resource.toStack(amount)
}
