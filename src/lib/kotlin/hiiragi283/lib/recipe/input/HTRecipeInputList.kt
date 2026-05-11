package hiiragi283.lib.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

data class HTRecipeInputList(private val input: RecipeInput) : AbstractList<ItemStack>() {
    override val size: Int get() = input.size()

    override fun get(index: Int): ItemStack = input.getItem(index)
}
