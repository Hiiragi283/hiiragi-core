package hiiragi283.lib.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

@JvmInline
value class HTItemListRecipeInput(val stacks: List<ItemStack>) : RecipeInput {
    constructor(vararg stacks: ItemStack) : this(stacks.toList())

    override fun getItem(index: Int): ItemStack = stacks[index]

    override fun size(): Int = stacks.size

    override fun isEmpty(): Boolean = stacks.isEmpty() || stacks.all(ItemStack::isEmpty)
}
