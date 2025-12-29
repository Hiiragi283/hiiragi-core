package hiiragi283.core.common.inventory

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.extractItem
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.api.storage.item.insert
import hiiragi283.core.api.storage.item.toResource
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import java.util.Optional
import java.util.function.BiConsumer
import kotlin.math.min

/**
 * [HTItemSlot]に基づいた[Slot]の実装
 * @see mekanism.common.inventory.container.slot.InventoryContainerSlot
 */
open class HTContainerItemSlot(
    val slot: HTItemSlot,
    x: Int,
    y: Int,
    private val stackSetter: BiConsumer<HTItemResourceType?, Int>,
    private val manualFilter: (HTItemResourceType, HTStorageAccess) -> Boolean,
    val slotType: Type,
) : Slot(emptyContainer, 0, x, y) {
    companion object {
        @JvmStatic
        private val emptyContainer = SimpleContainer(0)
    }

    constructor(slot: HTItemSlot.Basic, x: Int, y: Int, slotType: Type) : this(
        slot,
        x,
        y,
        { resource: HTItemResourceType?, count: Int ->
            slot.setResource(resource)
            slot.setAmount(count)
        },
        slot::isStackValidForInsert,
        slotType,
    )

    fun updateCount(count: Int) {
        stackSetter.accept(slot.getResource(), count)
        setChanged()
    }

    private fun insertItem(stack: ItemStack, action: HTStorageAction): ItemStack {
        val remainder: ItemStack = slot.insert(stack, action, HTStorageAccess.MANUAL)
        if (action.execute() && stack.count != remainder.count) {
            setChanged()
        }
        return remainder
    }

    override fun mayPlace(stack: ItemStack): Boolean {
        val resourceType: HTItemResourceType = stack.toResource() ?: return false
        if (slot.getResource() == null) {
            return insertItem(stack, HTStorageAction.SIMULATE).count < stack.count
        }
        if (slot.extract(1, HTStorageAction.SIMULATE, HTStorageAccess.MANUAL) == 0) return false
        return manualFilter(resourceType, HTStorageAccess.MANUAL)
    }

    override fun getItem(): ItemStack = slot.getItemStack()

    override fun hasItem(): Boolean = slot.getResource() != null

    override fun set(stack: ItemStack) {
        stackSetter.accept(stack.toResource(), stack.count)
        setChanged()
    }

    override fun setChanged() {
        super.setChanged()
        slot.onContentsChanged()
    }

    override fun getMaxStackSize(): Int = slot.getCapacity()

    override fun getMaxStackSize(stack: ItemStack): Int = slot.getCapacity(stack.toResource())

    override fun mayPickup(player: Player): Boolean = slot.extract(1, HTStorageAction.SIMULATE, HTStorageAccess.MANUAL) > 0

    override fun remove(amount: Int): ItemStack = slot.extractItem(amount, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)

    override fun tryRemove(count: Int, decrement: Int, player: Player): Optional<ItemStack> {
        if (allowPartialRemoval()) {
            if (!mayPickup(player)) {
                return Optional.empty()
            }
            val count: Int = min(count, decrement)
            val stack: ItemStack = remove(count)
            if (stack.isEmpty) {
                return Optional.empty()
            } else if (item.isEmpty) {
                setByPlayer(ItemStack.EMPTY, stack)
            }
            return Optional.of(stack)
        }

        return super.tryRemove(count, decrement, player)
    }

    protected open fun allowPartialRemoval(): Boolean = true

    enum class Type {
        INPUT,
        OUTPUT,
        BOTH,
    }
}
