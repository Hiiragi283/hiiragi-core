package hiiragi283.lib.item

import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.toTextResult
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike

//    ItemLike    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
@Suppress("DEPRECATION")
val ItemLike.isAir: Boolean get() = this.asItem().builtInRegistryHolder().isAir

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
val Holder<out ItemLike>.isAir: Boolean get() = this.`is`(vanillaId("air"))

//    ItemStackTemplate    //

/**
 * [ItemStackTemplate]が`null`の場合，[ItemStack.EMPTY]を返します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ItemStackTemplate?.createOrEmpty(): ItemStack = this?.create() ?: ItemStack.EMPTY

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun ItemStackTemplate.transmuteCopy(newItem: ItemLike, newCount: Int = this.count()): ItemStackTemplate? = when {
    newItem.isAir -> null
    else -> ItemStackTemplate(newItem.asItem(), newCount, this.components())
}

//    ItemStack    //

/**
 * [ItemStack]を[ItemStackTemplate]に変換します。
 * @return [isEmpty]の場合は`null`
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
fun ItemStack.toTemplateResult(): HTTextResult<ItemStackTemplate> = this.toTemplateOrNull().toTextResult { "ItemStack must be non-empty" }
