package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.transfer.ItemResourceHandler
import hiiragi283.core.api.transfer.item.toResourcePair
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTItemOutputHandler : HTOutputHandler<ItemStack> {
    companion object {
        @JvmStatic
        fun single(handler: ItemResourceHandler, index: Int): HTItemOutputHandler = Single(handler, index)

        @JvmStatic
        fun multiple(handler: ItemResourceHandler, indices: IntRange): HTItemOutputHandler = multiple(handler, indices.toList())

        @JvmStatic
        fun multiple(handler: ItemResourceHandler, indices: Collection<Int>): HTItemOutputHandler = multiple(handler, indices.toIntArray())

        @JvmStatic
        fun multiple(handler: ItemResourceHandler, indices: IntArray): HTItemOutputHandler = Multiple(handler, indices)
    }

    override fun getResultAmount(stack: ItemStack): Int = stack.count

    private class Single(private val handler: ItemResourceHandler, private val index: Int) : HTItemOutputHandler {
        override fun insert(stack: ItemStack, transaction: TransactionContext): Int {
            val (resource: ItemResource, amount: Int) = stack.toResourcePair()
            return handler.insert(index, resource, amount, transaction)
        }
    }

    private class Multiple(private val handler: ItemResourceHandler, private val indices: IntArray) : HTItemOutputHandler {
        override fun insert(stack: ItemStack, transaction: TransactionContext): Int {
            val (resource: ItemResource, amount: Int) = stack.toResourcePair()

            var inserted = 0
            for (i: Int in indices) {
                inserted += handler.insert(i, resource, amount - inserted, transaction)
                if (inserted == amount) break
            }
            return inserted
        }
    }
}
