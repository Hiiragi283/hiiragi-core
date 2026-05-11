package hiiragi283.lib.transfer.item

import com.google.common.base.Predicates
import hiiragi283.lib.transfer.HTHandlerAccess
import hiiragi283.lib.util.HTSimpleProperty
import java.util.function.Predicate
import kotlin.properties.ReadWriteProperty
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.item.ItemResource

class HTInputItemSlot(property: ReadWriteProperty<Any?, ItemStack>, limit: Int, filter: Predicate<ItemResource>, listener: Runnable?) : HTBasicItemSlot(property, limit, filter, listener) {
    companion object {
        @JvmStatic
        fun create(
            listener: Runnable?,
            limit: Int = Item.ABSOLUTE_MAX_STACK_SIZE,
            filter: Predicate<ItemResource> = Predicates.alwaysTrue(),
            property: ReadWriteProperty<Any?, ItemStack> = HTSimpleProperty(ItemStack.EMPTY),
        ): HTInputItemSlot {
            TransferPreconditions.checkNonNegative(limit)
            return HTInputItemSlot(property, limit, filter, listener)
        }
    }

    override fun canStackExtract(resource: ItemResource, access: HTHandlerAccess): Boolean = super.canStackExtract(resource, access) && access != HTHandlerAccess.EXTERNAL
}
