package hiiragi283.core.api.registry

import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[ItemLike]の拡張インターフェースです。
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
interface HTItemLike<out ITEM : Item> : ItemLike {
    override fun asItem(): ITEM

    /**
     * 指定した[item]と一致するか判定します。
     * @author Hiiragi Tsubasa
     * @since 0.14.0
     */
    fun isOf(item: Item): Boolean = this.asItem() == item

    // ItemStack
    fun isOf(stack: ItemStack): Boolean = stack.`is`(this.asItem())

    /**
     * 指定した[個数][count]で[ItemStack]に変換します。
     */
    fun toStack(count: Int = 1): ItemStack = ItemStack(this, count)

    // HTItemResourceType

    /**
     * [HTItemResourceType]に変換します。
     */
    fun toResource(): HTItemResourceType? = toStack().toResource()

    /**
     * 指定した[patch]で[HTItemResourceType]に変換します。
     */
    fun toResource(patch: DataComponentPatch): HTItemResourceType? {
        val stack: ItemStack = toStack()
        stack.applyComponents(patch)
        return stack.toResource()
    }
}
