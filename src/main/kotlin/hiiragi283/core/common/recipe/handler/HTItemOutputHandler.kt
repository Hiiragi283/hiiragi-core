package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.insert
import hiiragi283.core.util.HTStackSlotHelper
import net.minecraft.world.item.ItemStack

interface HTItemOutputHandler : HTOutputHandler<ItemStack> {
    companion object {
        @JvmStatic
        fun single(slot: HTItemSlot): HTItemOutputHandler = Single(slot)

        @JvmStatic
        fun multiple(vararg slots: HTItemSlot): HTItemOutputHandler = multiple(listOf(*slots))

        @JvmStatic
        fun multiple(slots: List<HTItemSlot>): HTItemOutputHandler = Multiple(slots)
    }

    private class Single(private val slot: HTItemSlot) : HTItemOutputHandler {
        override fun canInsert(stack: ItemStack): Boolean = slot.insert(stack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).isEmpty

        override fun insert(stack: ItemStack) {
            slot.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }

    private class Multiple(private val slots: List<HTItemSlot>) : HTItemOutputHandler {
        override fun canInsert(stack: ItemStack): Boolean = HTStackSlotHelper.insertStacks(
            slots,
            stack,
            HTStorageAction.SIMULATE,
            HTStorageAccess.INTERNAL,
        ) == 0

        override fun insert(stack: ItemStack) {
            HTStackSlotHelper.insertStacks(
                slots,
                stack,
                HTStorageAction.EXECUTE,
                HTStorageAccess.INTERNAL,
            )
        }
    }
}
