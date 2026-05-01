package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.base.predicate.HTDoubleRecipePredicate
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import net.minecraft.world.item.ItemStack

interface HTForgingRecipe :
    HTDoubleRecipePredicate.DoubleItem,
    HTRecipeFactory<HTDoubleRecipeInput, ItemStack>,
    HTProgressRecipe<HTDoubleRecipeInput>
