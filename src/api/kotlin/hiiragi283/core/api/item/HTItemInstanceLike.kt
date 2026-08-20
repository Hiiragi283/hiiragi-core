package hiiragi283.core.api.item

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack

/**
 * [ItemStack]に変換可能なオブジェクトを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
interface HTItemInstanceLike {
    /**
     * 新しい[ItemStack]のインスタンスを作成します。
     */
    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack
}
