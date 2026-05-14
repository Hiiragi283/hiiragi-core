package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.item.ItemResourceHandler
import hiiragi283.lib.transfer.item.getItemStack
import hiiragi283.lib.transfer.item.toResourcePair
import hiiragi283.lib.transfer.useTransaction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.transfer.IndexModifier
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTItemInputHandler(private val handler: ItemResourceHandler, private val index: Int, private val remainderConsumer: IndexModifier<ItemResource>? = null) : HTInputHandler<ItemStack> {
    override fun getStack(): ItemStack = handler.getItemStack(index)

    override fun extract(amount: Int, parent: TransactionContext?): Result<Int> = runCatching {
        if (remainderConsumer != null && handler.getAmountAsInt(index) == 1) {
            val stackIn: ItemStack = getStack()
            val remainder: ItemStackTemplate? = stackIn.craftingRemainder
            if (remainder != null) {
                val (resource: ItemResource, amount: Int) = remainder.toResourcePair()
                remainderConsumer.set(index, resource, amount)
                return@runCatching 1
            }
        }
        val resourceIn: ItemResource = handler.getResource(index)
        useTransaction(parent) { transaction: Transaction -> handler.extract(index, resourceIn, amount, transaction) }
    }
}
