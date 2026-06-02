package hiiragi283.lib.gui

import hiiragi283.lib.transfer.HTHandlerAccess
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.getItemStack
import hiiragi283.lib.transfer.useTransaction
import java.util.function.BiPredicate
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.world.inventory.StackCopySlot

class HTContainerItemSlot(val slot: HTItemSlot, private val stackSetter: (ItemStack) -> Unit, private val manualFilter: BiPredicate<ItemResource, HTHandlerAccess>, x: Int, y: Int, val slotType: HTBackgroundType) : StackCopySlot(0, x, y) {
    constructor(slot: HTBasicItemSlot, x: Int, y: Int, slotType: HTBackgroundType) : this(slot, { stack: ItemStack -> slot.setStack(stack, null) }, slot::isValidForInsertion, x, y, slotType)

    override fun getStackCopy(): ItemStack = slot.getItemStack()

    override fun setStackCopy(stack: ItemStack) {
        stackSetter(stack)
    }

    fun updateCount(count: Int) {
        stackCopy = stackCopy.copyWithCount(count)
    }

    override fun mayPlace(itemStack: ItemStack): Boolean = manualFilter.test(ItemResource.of(itemStack), HTHandlerAccess.MANUAL)

    override fun getMaxStackSize(): Int = slot.getCapacityAsInt(slot.resource)

    override fun getMaxStackSize(itemStack: ItemStack): Int = slot.getCapacityAsInt(ItemResource.of(itemStack))

    override fun mayPickup(player: Player): Boolean = when {
        slot.isEmpty() -> true
        else -> useTransaction { slot.extract(slot.resource, 1, it, HTHandlerAccess.MANUAL) } > 0
    }
}
