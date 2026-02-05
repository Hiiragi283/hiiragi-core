package hiiragi283.core.api.recipe.input

import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemView
import hiiragi283.core.api.storage.item.toResourcePair
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 複数の[アイテム][ItemStack]を不定形で保持する[RecipeInput]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
open class HTShapelessRecipeInput(val items: Map<HTItemResourceType, Int>) : RecipeInput {
    companion object {
        @JvmName("createMapFromStacks")
        @JvmStatic
        fun createMap(stacks: Iterable<ItemStack>): Map<HTItemResourceType, Int> =
            stacks.fold(hashMapOf()) { map: HashMap<HTItemResourceType, Int>, stack: ItemStack ->
                val (resource: HTItemResourceType, amount: Int) = stack.toResourcePair() ?: return@fold map
                map[resource] = (map[resource] ?: 0) + amount
                map
            }

        @JvmName("createMapFromViews")
        @JvmStatic
        fun createMap(views: Iterable<HTItemView>): Map<HTItemResourceType, Int> =
            views.fold(hashMapOf()) { map: HashMap<HTItemResourceType, Int>, view: HTItemView ->
                val resource: HTItemResourceType = view.getResource() ?: return@fold map
                map[resource] = (map[resource] ?: 0) + view.getAmount()
                map
            }
    }

    final override fun getItem(index: Int): ItemStack {
        val (resource: HTItemResourceType, count: Int) = items.entries.elementAt(index)
        return resource.toStack(count)
    }

    final override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.isEmpty()
}
