package hiiragi283.core.api.storage.item

import hiiragi283.core.api.stack.toImmutable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack

/**
 * [HTItemSlot]に基づいた[HTSidedItemHandler]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.inventory.IMekanismInventory
 */
fun interface HTItemHandler : HTSidedItemHandler {
    /**
     * このハンドラが有効か判定します。
     */
    fun hasItemHandler(): Boolean = true

    /**
     * 指定した[面][side]から[HTItemSlot]の一覧を取得します。
     */
    fun getItemSlots(side: Direction?): List<HTItemSlot>

    /**
     * 指定した[面][side]と[インデックス][slot]から[HTItemSlot]を取得します。
     * @return 指定した[インデックス][slot]が範囲外の場合は`null`
     */
    fun getItemSlot(slot: Int, side: Direction?): HTItemSlot? = getItemSlots(side).getOrNull(slot)

    override fun getStackInSlot(slot: Int, side: Direction?): ItemStack = getItemSlot(slot, side)?.getItemStack() ?: ItemStack.EMPTY

    override fun getSlots(side: Direction?): Int = getItemSlots(side).size

    override fun insertItem(
        slot: Int,
        stack: ItemStack,
        action: HTStorageAction,
        side: Direction?,
    ): ItemStack {
        val slot: HTItemSlot = getItemSlot(slot, side) ?: return stack
        return slot.insert(stack.toImmutable(), action, HTStorageAccess.forHandler(side))?.unwrap() ?: ItemStack.EMPTY
    }

    override fun extractItem(
        slot: Int,
        amount: Int,
        action: HTStorageAction,
        side: Direction?,
    ): ItemStack {
        val slot: HTItemSlot = getItemSlot(slot, side) ?: return ItemStack.EMPTY
        return slot.extract(amount, action, HTStorageAccess.forHandler(side))?.unwrap() ?: ItemStack.EMPTY
    }

    override fun getSlotLimit(slot: Int, side: Direction?): Int = getItemSlot(slot, side)?.getCapacity() ?: 0

    override fun isItemValid(slot: Int, stack: ItemStack, side: Direction?): Boolean = getItemSlot(slot, side)?.isValid(stack) ?: false
}
