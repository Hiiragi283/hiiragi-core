package hiiragi283.core.api.storage.item

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.resource.HTResourceSlot
import hiiragi283.core.api.storage.resource.HTResourceView
import net.minecraft.world.item.ItemStack

/**
 * [HTItemResourceType]向けの[HTResourceView]のエイリアス
 */
typealias HTItemView = HTResourceView<HTItemResourceType>

/**
 * [HTItemResourceType]向けの[HTResourceSlot]のエイリアス
 * @since 0.9.0
 */
typealias HTItemSlot = HTResourceSlot<HTItemResourceType>

/**
 * この[HTItemView][this]から[ItemStack]を取得します。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun HTItemView.getItemStack(): ItemStack = this.getResource().toStackOrEmpty(this.getAmount())

/**
 * この[HTItemSlot][this]に指定した[stack]を搬入します。
 * @return 搬入されない[ItemStack]
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun HTItemSlot.insert(stack: ItemStack, action: HTStorageAction, access: HTStorageAccess): ItemStack {
    val remainder: Int = this.insert(stack.toResource(), stack.count, action, access)
    return stack.copyWithCount(remainder)
}

/**
 * この[HTItemSlot][this]から指定した[amount]だけ搬出します。
 * @return 搬出される[ItemStack]
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun HTItemSlot.extractItem(amount: Int, action: HTStorageAction, access: HTStorageAccess): ItemStack {
    val resourceIn: HTItemResourceType = this.getResource() ?: return ItemStack.EMPTY
    return this.extract(amount, action, access).let(resourceIn::toStack)
}
