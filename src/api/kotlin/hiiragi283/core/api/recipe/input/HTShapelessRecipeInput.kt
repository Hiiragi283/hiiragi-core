package hiiragi283.core.api.recipe.input

import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 複数の[アイテム][ItemStack]を不定形で保持する[RecipeInput]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
@JvmRecord
data class HTShapelessRecipeInput(val items: Map<HTItemResourceType, Int>) : RecipeInput {
    override fun getItem(index: Int): ItemStack {
        val (resource: HTItemResourceType, count: Int) = items.entries.elementAt(index)
        return resource.toStack(count)
    }

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.isEmpty()
}
