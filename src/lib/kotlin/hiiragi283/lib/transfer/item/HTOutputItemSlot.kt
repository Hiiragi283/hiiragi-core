package hiiragi283.lib.transfer.item

import com.google.common.base.Predicates
import hiiragi283.lib.transfer.HTHandlerAccess
import hiiragi283.lib.util.HTSimpleProperty
import kotlin.properties.ReadWriteProperty
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource

class HTOutputItemSlot(property: ReadWriteProperty<Any?, ItemStack>, listener: Runnable?) : HTBasicItemSlot(property, Item.ABSOLUTE_MAX_STACK_SIZE, Predicates.alwaysTrue(), listener) {
    companion object {
        @JvmStatic
        fun create(
            listener: Runnable?,
            property: ReadWriteProperty<Any?, ItemStack> = HTSimpleProperty(ItemStack.EMPTY),
        ): HTOutputItemSlot = HTOutputItemSlot(property, listener)
    }

    override fun isStackValidForInsert(resource: ItemResource, access: HTHandlerAccess): Boolean = super.isStackValidForInsert(resource, access) && access == HTHandlerAccess.INTERNAL
}
