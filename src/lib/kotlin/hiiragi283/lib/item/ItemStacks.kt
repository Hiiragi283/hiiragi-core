package hiiragi283.lib.item

import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.right
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate

//    ItemStackTemplate    //

fun ItemStackTemplate?.createOrEmpty(): ItemStack = this?.create() ?: ItemStack.EMPTY

//    ItemStack    //

fun ItemStack.toTemplateOrNull(): ItemStackTemplate? = when {
    this.isEmpty -> null
    else -> ItemStackTemplate.fromNonEmptyStack(this)
}

fun ItemStack.toTemplateResult(): HTTextResult<ItemStackTemplate> = this.toTemplateOrNull()?.right() ?: HTTextResult("ItemStack must be non-empty")
