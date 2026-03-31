package hiiragi283.core.api.transfer.holder

import hiiragi283.core.api.transfer.HTResourceSlot
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.resource.Resource

/**
 * [HTResourceSlot]向けの[HTCapabilityHolder]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.capabilities.holder.slot.IInventorySlotHolder
 */
interface HTResourceSlotHolder<T : Resource> : HTCapabilityHolder {
    /**
     * 指定された[面][side]から[HTResourceSlot]の一覧を取得します。
     */
    fun getSlots(side: Direction?): List<HTResourceSlot<T>>
}
