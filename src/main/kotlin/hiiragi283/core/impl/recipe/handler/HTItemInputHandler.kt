package hiiragi283.core.impl.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.api.storage.item.toResource
import java.util.function.Consumer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class HTItemInputHandler(private val slot: HTItemSlot, private val remainderConsumer: Consumer<ItemStack>? = null) :
    HTInputHandler<ItemStack>,
    HTItemSlot by slot {
    fun consume(ingredient: Ingredient) {
        when {
            ingredient.test(this.getItemStack()) -> consume(1)
            else -> return
        }
    }

    override fun getStack(): ItemStack = this.getItemStack()

    override fun consume(amount: Int) {
        if (amount > 0) {
            if (remainderConsumer != null && getAmount() == 1) {
                val stackIn: ItemStack = getStack()
                if (stackIn.hasCraftingRemainingItem()) {
                    remainderConsumer.accept(stackIn.craftingRemainingItem)
                    return
                }
            }
            extract(amount, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }

    override fun consume(stack: ItemStack) {
        if (!stack.isEmpty) {
            if (remainderConsumer != null && getAmount() == 1) {
                val stackIn: ItemStack = getStack()
                if (stackIn.hasCraftingRemainingItem()) {
                    remainderConsumer.accept(stackIn.craftingRemainingItem)
                    return
                }
            }
            extract(stack.toResource(), stack.count, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }

    override fun getCapacity(): Int = slot.getCapacity()

    override fun isEmpty(): Boolean = slot.isEmpty()
}
