package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.testOnlyType
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.HTSlotModifier
import hiiragi283.core.api.transfer.item.HTItemSlot
import hiiragi283.core.api.transfer.item.stack
import hiiragi283.core.api.transfer.set
import hiiragi283.core.api.transfer.useTransaction
import hiiragi283.core.impl.transfer.HTBasicResourceSlot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.transfer.item.ItemResource

class HTItemInputHandler(slot: HTItemSlot, private val remainderConsumer: HTSlotModifier<ItemResource>? = null) :
    HTInputHandler<SizedIngredient>,
    HTItemSlot by slot {
    constructor(slot: HTBasicResourceSlot<ItemResource>) : this(slot, slot)

    override fun getMatchingAmount(ingredient: SizedIngredient): Int = when {
        ingredient.testOnlyType(this.resource) -> ingredient.count()
        else -> 0
    }

    override fun consume(amount: Int) {
        if (amount > 0) {
            if (remainderConsumer != null && this.amountAsLong == 1L) {
                val remainder: ItemStack? = this.stack.craftingRemainder?.create()
                if (remainder != null) {
                    remainderConsumer.set(remainder)
                    return
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
