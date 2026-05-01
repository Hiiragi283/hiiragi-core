package hiiragi283.core.api.recipe.base

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTCrushingRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<Iterable<ItemStack>>,
    HTProgressRecipe<SingleRecipeInput>
