package hiiragi283.core.api.storage.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.storage.resource.HTResourceSlot
import net.minecraft.world.item.ItemStack
import kotlin.math.min

/**
 * [HTItemResourceType]向けの[HTResourceSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
interface HTItemSlot : HTResourceSlot<HTItemResourceType> {
    companion object {
        /**
         * 指定した[resource]からスロットの容量を取得します。
         * @return [resource]が`null`の場合は[limit]，それ以外の場合は[ItemStack.maxStackSize]
         */
        @JvmStatic
        fun getMaxStackSize(resource: HTItemResourceType?, limit: Int = HTConst.ABSOLUTE_MAX_STACK_SIZE): Int =
            if (resource == null) limit else min(limit, resource.toStack().maxStackSize)
    }

    //    Basic    //

    /**
     * [HTItemResourceType]向けの[HTResourceSlot.Basic]の拡張クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.4.0
     */
    abstract class Basic :
        HTResourceSlot.Basic<HTItemResourceType>(),
        HTItemSlot {
        fun setStack(stack: ItemStack) {
            setResource(stack.toResource())
            setAmount(stack.count)
        }

        override fun toString(): String = "HTItemSlot(resource=${getResource()}, amount=${getAmount()}, capacity=${getCapacity()})"
    }
}
