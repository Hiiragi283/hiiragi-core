package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.progress.HTProgressProvider
import net.minecraft.world.item.ItemStack

interface HTItemToMultiItemRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<Iterable<ItemStack>>,
    HTProgressProvider<ItemStack>
