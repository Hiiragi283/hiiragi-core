package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.progress.HTBiProgressProvider
import net.minecraft.world.item.ItemStack

interface HTDoubleItemToItemRecipe :
    HTRecipePredicates.DoubleItem,
    HTRecipeFactories.DoubleItem<ItemStack>,
    HTBiProgressProvider<ItemStack, ItemStack>
