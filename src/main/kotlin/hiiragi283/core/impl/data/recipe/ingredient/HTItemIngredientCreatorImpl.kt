package hiiragi283.core.impl.data.recipe.ingredient

import hiiragi283.core.api.data.recipe.ingredient.HTItemIngredientCreator
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient

internal class HTItemIngredientCreatorImpl(getter: HolderGetter<Item>) :
    HTRegisteredIngredientCreatorImpl<Item, Ingredient>(getter),
    HTItemIngredientCreator {
    @Suppress("DEPRECATION")
    override fun getHolder(type: Item): Holder<Item> = type.builtInRegistryHolder()
}
