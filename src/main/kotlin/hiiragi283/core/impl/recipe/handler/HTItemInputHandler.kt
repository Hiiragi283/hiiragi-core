package hiiragi283.core.impl.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.getItemStack
import net.minecraft.world.item.ItemStack
import java.util.function.Consumer

class HTItemInputHandler(slot: HTItemSlot, private val remainderConsumer: Consumer<ItemStack>? = null) :
    HTInputHandler<HTItemIngredient>,
    HTItemSlot by slot {
    override fun getMatchingAmount(ingredient: HTItemIngredient): Int = ingredient.getRequiredAmount(this.getItemStack())

    override fun consume(amount: Int) {
        if (amount > 0) {
            if (remainderConsumer != null && getAmount() == 1) {
                val stackIn: ItemStack = getItemStack()
                if (stackIn.hasCraftingRemainingItem()) {
                    remainderConsumer.accept(stackIn.craftingRemainingItem)
                    return
                }
            }
            extract(amount, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }
}
