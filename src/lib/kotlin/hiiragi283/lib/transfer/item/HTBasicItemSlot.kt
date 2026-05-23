package hiiragi283.lib.transfer.item

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.HTBasicResourceSlot
import hiiragi283.lib.transfer.HTHandlerAccess
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource

open class HTBasicItemSlot(capacity: Long, canInsert: BiPredicate<ItemResource, HTHandlerAccess>, canExtract: BiPredicate<ItemResource, HTHandlerAccess>, filter: Predicate<ItemResource>, listener: Runnable?) : HTBasicResourceSlot<ItemResource>(capacity, canInsert, canExtract, filter, listener, ItemResource.EMPTY) {
    override fun getCapacityAsLong(resource: ItemResource): Long {
        val capacity: Long = super.getCapacityAsLong(resource)
        return when {
            isEmpty() -> capacity
            else -> minOf(capacity, resource.maxStackSize.toLong())
        }
    }

    final override fun serialize(output: ValueOutput) {
        output.store(HTConstants.ITEM, ItemResource.CODEC, this.resourceIn)
        output.putLong(HTConstants.AMOUNT, this.amountIn)
    }

    final override fun deserialize(input: ValueInput) {
        input.read(HTConstants.ITEM, ItemResource.CODEC).ifPresent { resource: ItemResource ->
            val amount: Long = input.getLongOr(HTConstants.AMOUNT, 0)
            if (amount > 0) {
                this.resourceIn = resource
                this.amountIn = amount
            }
        }
    }
}
