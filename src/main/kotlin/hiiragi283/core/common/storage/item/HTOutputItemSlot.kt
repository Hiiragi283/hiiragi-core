package hiiragi283.core.common.storage.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.HTStoragePredicates

/**
 * @see mekanism.common.inventory.slot.OutputInventorySlot
 */
class HTOutputItemSlot private constructor(listener: HTContentListener?, x: Int, y: Int) :
    HTBasicItemSlot(
        HTConst.ABSOLUTE_MAX_STACK_SIZE,
        HTStoragePredicates.alwaysTrueBi(),
        HTStoragePredicates.internalOnly(),
        HTStoragePredicates.alwaysTrue(),
        listener,
    ) {
        companion object {
            @JvmStatic
            fun create(listener: HTContentListener?, x: Int, y: Int): HTOutputItemSlot = HTOutputItemSlot(listener, x, y)
        }
    }
