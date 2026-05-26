package hiiragi283.lib.recipe.base

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTItemToChancedItemsRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<List<ItemStack>>,
    HTProgressRecipe<SingleRecipeInput>
