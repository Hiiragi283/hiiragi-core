package hiiragi283.core.api.recipe.input

import hiiragi283.core.api.tag.HiiragiCoreTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 複数の[アイテム][ItemStack]を保持する[RecipeInput]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
class HTListItemRecipeInput(private val items: List<ItemStack>) : RecipeInput {
    private fun validateItem(index: Int): ItemStack = items
        .getOrNull(index)
        ?.takeUnless { stack: ItemStack -> stack.`is`(HiiragiCoreTags.IGNORED_IN_RECIPE_INPUT) }
        ?: ItemStack.EMPTY

    override fun getItem(index: Int): ItemStack = validateItem(index)

    override fun size(): Int = items.size
}
