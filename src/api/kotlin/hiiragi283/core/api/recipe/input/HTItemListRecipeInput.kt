package hiiragi283.core.api.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

@JvmRecord
data class HTItemListRecipeInput(val items: List<ItemStack>) : RecipeInput {
    constructor(vararg items: ItemStack) : this(items.toList())

    override fun getItem(index: Int): ItemStack = items[index]

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.isEmpty()
}
