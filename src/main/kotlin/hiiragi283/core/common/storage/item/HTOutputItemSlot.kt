package hiiragi283.core.common.storage.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.HTStoragePredicates

class HTOutputItemSlot(listener: HTContentListener?) :
    HTBasicItemSlot(
        HTConst.ABSOLUTE_MAX_STACK_SIZE,
        HTStoragePredicates.alwaysTrueBi(),
        HTStoragePredicates.internalOnly(),
        HTStoragePredicates.alwaysTrue(),
        listener,
    )
