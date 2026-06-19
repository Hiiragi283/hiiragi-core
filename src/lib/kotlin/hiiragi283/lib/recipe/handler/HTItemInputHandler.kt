package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.HTHandlerAccess
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.getItemStack
import hiiragi283.lib.transfer.item.toResourcePair
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

typealias RemainderSetter = (ItemResource, Int) -> Unit

/**
 * [ItemStack]向けの[HTInputHandler]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTItemInputHandler(slot: HTItemSlot, private val remainderConsumer: RemainderSetter? = null) :
    HTInputHandler<ItemStack>,
    HTItemSlot by slot {
    override fun extract(amount: Int, transaction: TransactionContext): Result<Int> = runCatching {
        if (remainderConsumer != null && this.amountAsInt == 1) {
            val remainder: ItemStackTemplate? = getItemStack().craftingRemainder
            if (remainder != null) {
                val (resource: ItemResource, amount: Int) = remainder.toResourcePair()
                remainderConsumer(resource, amount)
                return@runCatching 1
            }
        }
        this.extract(this.resource, amount, transaction, HTHandlerAccess.INTERNAL)
    }
}
