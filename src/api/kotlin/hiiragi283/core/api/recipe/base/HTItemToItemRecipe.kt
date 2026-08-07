package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.progress.HTProgressProvider
import net.minecraft.world.item.ItemStack

interface HTItemToItemRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<ItemStack>,
    HTProgressProvider<ItemStack>
