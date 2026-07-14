package hiiragi283.core.api.item

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
typealias HTSimpleItemLike = HTItemLike<Item>

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
interface HTItemLike<out ITEM : Item> : ItemLike {
    override fun asItem(): ITEM

    /**
     * 新しい[ItemStack]のインスタンスを作成します。
     */
    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack
}
