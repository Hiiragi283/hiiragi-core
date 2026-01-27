package hiiragi283.core.api.item

import hiiragi283.core.api.data.buildDataPatch
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.ItemLike

//    ItemStack    //

fun <T : Any> createItemStack(
    item: ItemLike?,
    type: DataComponentType<T>,
    value: T,
    count: Int = 1,
): ItemStack = createItemStack(item, count, buildDataPatch { set(type, value) })

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

fun createEnchantedBook(enchantment: Holder<Enchantment>, level: Int = enchantment.value().maxLevel): ItemStack {
    val stack = ItemStack(Items.ENCHANTED_BOOK)
    stack.enchant(enchantment, level)
    return stack
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
fun createEnchantedBook(enchantments: ItemEnchantments): ItemStack =
    createItemStack(Items.ENCHANTED_BOOK, DataComponents.ENCHANTMENTS, enchantments)
