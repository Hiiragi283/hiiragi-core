package hiiragi283.lib.transfer.item

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.readOption
import hiiragi283.lib.transfer.HTBasicResourceSlot
import hiiragi283.lib.transfer.HTHandlerAccess
import hiiragi283.lib.transfer.HTResourceStack
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource

open class HTBasicItemSlot(capacity: Long, canInsert: BiPredicate<ItemResource, HTHandlerAccess>, canExtract: BiPredicate<ItemResource, HTHandlerAccess>, filter: Predicate<ItemResource>, listener: Runnable?) : HTBasicResourceSlot<ItemResource>(capacity, canInsert, canExtract, filter, listener, ItemResource.EMPTY) {
    companion object {
        @JvmField
        val CODEC: Codec<HTResourceStack<ItemResource>> = HTResourceStack.codec(ItemResource.CODEC)
    }

    override fun getCapacityAsLong(resource: ItemResource): Long {
        val capacity: Long = super.getCapacityAsLong(resource)
        return when {
            isEmpty() -> capacity
            else -> minOf(capacity, resource.maxStackSize.toLong())
        }
    }

    final override fun serialize(output: ValueOutput) {
        output.storeNullable(HTConstants.ITEM, CODEC, this.stackIn)
    }

    final override fun deserialize(input: ValueInput) {
        this.stackIn = input.readOption(HTConstants.ITEM, CODEC).getOrNull()
    }
}
