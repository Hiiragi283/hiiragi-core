package hiiragi283.lib.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

//    RecipeInput    //

/**
 * 有効なアイテムのインデックスの範囲
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
val RecipeInput.indices: IntRange get() = (0..<this.size())

/**
 * アイテムの[List]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun RecipeInput.asList(): List<ItemStack> = object : AbstractList<ItemStack>() {
    override val size: Int get() = this@asList.size()

    override fun get(index: Int): ItemStack = this@asList.getItem(index)

    override fun isEmpty(): Boolean = this@asList.isEmpty
}
