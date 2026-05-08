package hiiragi283.core.common.storage.item

import hiiragi283.core.api.storage.item.HTItemHandler
import hiiragi283.core.api.storage.item.HTItemSlot
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack

/**
 * @see net.neoforged.neoforge.items.ComponentItemHandler
 */
class HTComponentItemHandler(val container: ItemStack, val size: Int, private val factory: HTComponentItemHandler.Factory) : HTItemHandler {
    override fun getItemSlots(side: Direction?): List<HTItemSlot> = List(size) { index: Int -> factory.createSlot(container, size, index) }

    fun interface Factory {
        fun createSlot(container: ItemStack, size: Int, index: Int): HTItemSlot
    }
}
