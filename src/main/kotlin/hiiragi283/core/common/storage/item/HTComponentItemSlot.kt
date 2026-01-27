package hiiragi283.core.common.storage.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.storage.item.toStackOrEmpty
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.core.common.storage.component.HTComponentHandler
import hiiragi283.core.common.storage.component.HTComponentSlot
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see mekanism.common.attachments.containers.item.ComponentBackedInventorySlot
 */
class HTComponentItemSlot(
    attachedTo: ItemStack,
    size: Int,
    slot: Int,
    limit: Int,
    canExtract: BiPredicate<HTItemResourceType, HTStorageAccess>,
    canInsert: BiPredicate<HTItemResourceType, HTStorageAccess>,
    filter: Predicate<HTItemResourceType>,
) : HTComponentSlot<HTItemResourceType, ItemStack, HTAttachedItems>(attachedTo, size, slot, limit, canExtract, canInsert, filter),
    HTItemSlot {
    companion object {
        @JvmStatic
        fun create(
            context: HTComponentHandler.ContainerContext,
            limit: Int = HTConst.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<HTItemResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            canInsert: BiPredicate<HTItemResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            filter: Predicate<HTItemResourceType> = HTStoragePredicates.alwaysTrue(),
        ): HTComponentItemSlot = HTComponentItemSlot(context.attachedTo, context.size, context.index, limit, canExtract, canInsert, filter)
    }

    override fun capabilityCodec(): HTCapabilityCodec<HTItemSlot, HTAttachedItems> = HTCapabilityCodec.ITEM

    override fun getAmount(stack: ItemStack): Int = stack.count

    override fun fromStack(stack: ItemStack): HTItemResourceType? = stack.toResource()

    override fun createStack(resource: HTItemResourceType?, amount: Int): ItemStack = resource.toStackOrEmpty(amount)
}
