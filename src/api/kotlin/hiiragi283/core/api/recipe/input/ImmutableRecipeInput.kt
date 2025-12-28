package hiiragi283.core.api.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [RecipeInput]を[ItemStack]の[List]として扱うためのラッパークラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
class ImmutableRecipeInput(private val input: RecipeInput) : AbstractList<ItemStack>() {
    override val size: Int
        get() = input.size()

    override fun get(index: Int): ItemStack = input.getItem(index)
}
