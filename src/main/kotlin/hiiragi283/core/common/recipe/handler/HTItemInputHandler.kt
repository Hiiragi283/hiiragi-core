package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.testOnlyType
import hiiragi283.core.api.transfer.ItemResourceHandler
import hiiragi283.core.api.transfer.getStack
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.transfer.IndexModifier
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTItemInputHandler(
    private val handler: ItemResourceHandler,
    private val index: Int,
    private val remainderConsumer: IndexModifier<ItemResource>? = null,
) : HTInputHandler<SizedIngredient> {
    override fun getMatchingAmount(ingredient: SizedIngredient): Int = when {
        ingredient.testOnlyType(handler.getResource(index)) -> ingredient.count()
        else -> 0
    }

    override fun consume(amount: Int, transaction: TransactionContext) {
        if (amount > 0) {
            if (remainderConsumer != null && handler.getAmountAsLong(index) == 1L) {
                val stackIn: ItemStack = handler.getStack(index)
                val remainder: ItemStack? = stackIn.craftingRemainder?.create()
                if (remainder != null) {
                    remainderConsumer.set(index, ItemResource.of(remainder), remainder.count)
                    return
                }
            }
            val resource: ItemResource = handler.getResource(index)
            if (resource.isEmpty) return
            handler.extract(index, resource, amount, transaction)
        }
    }
}
