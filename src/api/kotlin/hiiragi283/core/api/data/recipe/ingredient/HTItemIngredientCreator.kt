package hiiragi283.core.api.data.recipe.ingredient

import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

interface HTItemIngredientCreator : HTIngredientCreator.Registered<Item, Ingredient> {
    // ItemLike
    fun fromItem(item: ItemLike): Ingredient = from(item.asItem())

    fun fromItems(items: Collection<ItemLike>): Ingredient = from(items.map(ItemLike::asItem))
}
