package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.testOnlyType
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.StrictResourceHandler
import hiiragi283.core.api.transfer.StrictResourceSlot
import hiiragi283.core.api.transfer.stack
import hiiragi283.core.api.transfer.useTransaction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.transfer.IndexModifier
import net.neoforged.neoforge.transfer.item.ItemResource

class HTItemInputHandler(slot: StrictResourceSlot<ItemResource>, private val remainderConsumer: IndexModifier<ItemResource>? = null) :
    HTInputHandler<SizedIngredient>,
    StrictResourceSlot<ItemResource> by slot {
    constructor(
        handler: StrictResourceHandler<ItemResource>,
        index: Int,
        remainderConsumer: IndexModifier<ItemResource>? = null,
    ) : this(StrictResourceSlot.of(handler, index), remainderConsumer)

    override fun getMatchingAmount(ingredient: SizedIngredient): Int = when {
        ingredient.testOnlyType(this.resource) -> ingredient.count()
        else -> 0
    }

    override fun consume(amount: Int) {
        if (amount > 0) {
            if (remainderConsumer != null && this.amountAsLong == 1L) {
                val stackIn: ItemStack = this.stack
                val remainder: ItemStack? = stackIn.craftingRemainder?.create()
                if (remainder != null) {
                    remainderConsumer.set(this.index, ItemResource.of(remainder), remainder.count)
                }
            }
            useTransaction {
                val resource: ItemResource = this.resource
                if (resource.isEmpty) return@useTransaction
                this.extract(resource, amount, it, HTHandlerAccess.INTERNAL)
                it.commit()
            }
        }
    }
}
