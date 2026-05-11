package hiiragi283.lib.item

import hiiragi283.lib.data.buildDataPatch
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike

//    ItemStackTemplate    //

fun <T : Any> createItemTemplate(item: ItemLike, type: DataComponentType<T>, value: T, count: Int = 1): Result<ItemStackTemplate> = createItemTemplate(item, count, buildDataPatch { set(type, value) })

fun createItemTemplate(item: ItemLike, count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): Result<ItemStackTemplate> = runCatching { ItemStackTemplate(item.asItem(), count, patch) }

//    ItemStack    //

fun <T : Any> createItemStack(item: ItemLike?, type: DataComponentType<T>, value: T, count: Int = 1): ItemStack = createItemStack(item, count, buildDataPatch { set(type, value) })

/**
 * 指定した引数から新しい[ItemStack]のインスタンスを作成します。
 * @param item アイテムの種類
 * @param count アイテムの量
 * @param patch 適応するコンポーネントの差分
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
fun createItemStack(item: ItemLike?, count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack {
    if (item == null) return ItemStack.EMPTY
    val stack = ItemStack(item, count)
    if (stack.isEmpty) return ItemStack.EMPTY
    stack.applyComponents(patch)
    return stack
}
