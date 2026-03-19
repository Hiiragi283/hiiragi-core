package hiiragi283.core.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items

class HCRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(registries, output) {
    override fun buildRecipes() {
        // Warped Wart
        shapeless(RecipeCategory.FOOD, HCBlocks.WARPED_WART, 9)
            .requires(Items.WARPED_WART_BLOCK)
            .unlockedBy(getHasName(Items.WARPED_WART_BLOCK), has(Items.WARPED_WART_BLOCK))
            .save(output, Registries.RECIPE.createKey(HCBlocks.WARPED_WART.getId()))
    }
}
