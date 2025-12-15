package hiiragi283.core.api.item

import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

fun ItemLike.toStack(count: Int = 1): ItemStack {
    val stack: ItemStack = asItem().defaultInstance
    check(!stack.isEmpty) { "Obtained empty item stack; incorrect getDefaultInstance() call?" }
    stack.count = count
    return stack
}

@Suppress("DEPRECATION")
fun ItemLike.builtInRegistryHolder(): Holder.Reference<Item> = this.asItem().builtInRegistryHolder()
