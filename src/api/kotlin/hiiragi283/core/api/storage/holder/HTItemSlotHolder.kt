package hiiragi283.core.api.storage.holder

import hiiragi283.core.api.storage.item.HTItemSlot
import net.minecraft.core.Direction

/**
 * [HTItemSlot]向けの[HTCapabilityHolder]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.capabilities.holder.slot.IInventorySlotHolder
 */
interface HTItemSlotHolder : HTCapabilityHolder {
    /**
     * 指定された[面][side]から[HTItemSlot]の一覧を取得します。
     */
    fun getItemSlot(side: Direction?): List<HTItemSlot>
}
