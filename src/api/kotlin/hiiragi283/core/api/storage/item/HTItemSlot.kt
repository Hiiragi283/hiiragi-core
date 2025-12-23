package hiiragi283.core.api.storage.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.api.stack.maxStackSize
import hiiragi283.core.api.storage.stack.HTStackSlot
import net.minecraft.world.item.ItemStack
import kotlin.math.min

/**
 * [ImmutableItemStack]向けの[HTStackSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.inventory.IInventorySlot
 */
interface HTItemSlot : HTStackSlot<ImmutableItemStack> {
    companion object {
        /**
         * 指定した[stack]からスロットの容量を取得します。
         * @return [stack]が`null`の場合は[limit]，それ以外の場合は[ImmutableItemStack.maxStackSize]
         */
        @JvmStatic
        fun getMaxStackSize(stack: ImmutableItemStack?, limit: Int = HTConst.ABSOLUTE_MAX_STACK_SIZE): Int =
            if (stack == null) limit else min(limit, stack.maxStackSize())
    }

    override fun isSameStack(other: ImmutableItemStack?): Boolean = ItemStack.isSameItemSameComponents(
        this.getItemStack(),
        other?.unwrap() ?: ItemStack.EMPTY,
    )

    //    Basic    //

    /**
     * [ImmutableItemStack]向けの[HTStackSlot.Basic]の拡張クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    abstract class Basic :
        HTStackSlot.Basic<ImmutableItemStack>(),
        HTItemSlot {
        override fun toString(): String = "HTItemSlot(stack=${getStack()}, capacity=${getCapacity()})"
    }
}
