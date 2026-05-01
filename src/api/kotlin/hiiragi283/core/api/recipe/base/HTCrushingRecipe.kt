package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.base.factory.HTMultiRecipeFactory
import hiiragi283.core.api.recipe.base.predicate.HTSingleRecipePredicate
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTCrushingRecipe :
    HTSingleRecipePredicate.SingleItem,
    HTMultiRecipeFactory.ItemTo<Iterable<ItemStack>>,
    HTProgressRecipe<SingleRecipeInput>
