package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.getItemStack
import net.minecraft.world.item.ItemStack
import java.util.function.Consumer

class HTItemInputHandler(slot: HTItemSlot, private val remainderConsumer: Consumer<ItemStack>? = null) :
    HTInputHandler<HTItemResourceType>,
    HTItemSlot by slot {
    override fun getMatchingAmount(ingredient: HTIngredient<HTItemResourceType>): Int {
        val resource: HTItemResourceType = getResource() ?: return 0
        if (ingredient.testOnlyType(resource)) {
            return ingredient.amount
        }
        return 0
    }

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
