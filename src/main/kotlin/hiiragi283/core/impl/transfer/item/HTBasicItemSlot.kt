package hiiragi283.core.impl.transfer.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.HTTransferPredicates
import hiiragi283.core.api.transfer.item.HTItemResourceConverter
import hiiragi283.core.impl.transfer.HTBasicResourceSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.function.BiPredicate
import java.util.function.Predicate

open class HTBasicItemSlot(
    private val limit: Int,
    canExtract: BiPredicate<ItemResource, HTHandlerAccess>,
    canInsert: BiPredicate<ItemResource, HTHandlerAccess>,
    filter: Predicate<ItemResource>,
    listener: HTContentListener?,
) : HTBasicResourceSlot.Stacked<ItemStack, ItemResource>(
        HTConst.ITEM,
        ItemStack.CODEC,
        canExtract,
        canInsert,
        filter,
        listener,
    ) {
    companion object {
        @JvmStatic
        private fun validateLimit(limit: Int): Int {
            check(limit >= 0) { "Limit must be non negative" }
            return limit
        }

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            limit: Int = Item.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<ItemResource, HTHandlerAccess> = HTTransferPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ItemResource, HTHandlerAccess> = HTTransferPredicates.alwaysTrueBi(),
            filter: Predicate<ItemResource> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicItemSlot = HTBasicItemSlot(validateLimit(limit), canExtract, canInsert, filter, listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            limit: Int = Item.ABSOLUTE_MAX_STACK_SIZE,
            canInsert: Predicate<ItemResource> = HTTransferPredicates.alwaysTrue(),
            filter: Predicate<ItemResource> = canInsert,
        ): HTBasicItemSlot = create(
            listener,
            limit,
            HTTransferPredicates.notExternal(),
            { stack: ItemResource, _ -> canInsert.test(stack) },
            filter,
        )

        @JvmStatic
        fun output(listener: HTContentListener?): HTBasicItemSlot = create(
            listener,
            canInsert = HTTransferPredicates.internalOnly(),
        )
    }

    //    HTBasicResourceSlot    //

    override fun getConverter(): HTItemResourceConverter = HTItemResourceConverter

    override fun getCapacityAsLong(resource: ItemResource): Long = when {
        resource.isEmpty -> limit
        else -> minOf(resource.maxStackSize, limit)
    }.toLong()
}
