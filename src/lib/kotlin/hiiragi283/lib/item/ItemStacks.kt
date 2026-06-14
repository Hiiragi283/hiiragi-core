package hiiragi283.lib.item

import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.right
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate

//    ItemStackTemplate    //

/**
 * [ItemStackTemplate]が`null`の場合，[ItemStack.EMPTY]を返します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemStackTemplate?.createOrEmpty(): ItemStack = this?.create() ?: ItemStack.EMPTY

//    ItemStack    //

/**
 * [ItemStack]を[ItemStackTemplate]に変換します。
 * @return [ItemStack.isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemStack.toTemplateOrNull(): ItemStackTemplate? = when {
    this.isEmpty -> null
    else -> ItemStackTemplate.fromNonEmptyStack(this)
}

/**
 * [ItemStack]を[ItemStackTemplate]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemStack.toTemplateResult(): HTTextResult<ItemStackTemplate> = this.toTemplateOrNull()?.right() ?: HTTextResult("ItemStack must be non-empty")
