package hiiragi283.lib.item

import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.getOrElse
import hiiragi283.lib.util.right
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

//    ItemStackTemplate    //

fun <T : Any> createItemTemplate(item: ItemLike, type: DataComponentType<T>, value: T, count: Int = 1): HTTextResult<ItemStackTemplate> = createItemTemplate(item, count, buildDataPatch { set(type, value) })

fun createItemTemplate(item: ItemLike, count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): HTTextResult<ItemStackTemplate> = when (val item1: Item = item.asItem()) {
    Items.AIR -> HTTextResult("Item $item must be non-empty")
    else -> ItemStackTemplate(item1, count, patch).right()
}

fun ItemStackTemplate?.createOrEmpty(): ItemStack = this?.create() ?: ItemStack.EMPTY

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
    return createItemTemplate(item, count, patch).map(ItemStackTemplate::create).getOrElse { ItemStack.EMPTY }
}

fun ItemStack.toTemplateOrNull(): ItemStackTemplate? = when {
    this.isEmpty -> null
    else -> ItemStackTemplate.fromNonEmptyStack(this)
}

fun ItemStack.toTemplateResult(): HTTextResult<ItemStackTemplate> = this.toTemplateOrNull()?.right() ?: HTTextResult("ItemStack must be non-empty")
