package hiiragi283.core.api.recipe.base

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

interface HTDoubleItemToItemRecipe :
    HTRecipePredicates.DoubleItem,
    HTRecipeFactories.DoubleItem<ItemStack>,
    HTProgressRecipe<RecipeInput>
