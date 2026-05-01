package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import net.minecraft.world.item.ItemStack

interface HTForgingRecipe :
    HTRecipePredicates.DoubleItem,
    HTRecipeFactories.DoubleItem<ItemStack>,
    HTProgressRecipe<HTDoubleRecipeInput>
