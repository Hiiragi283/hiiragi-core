package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.registry.HTFluidContent
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

class HCTankInteractionRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {}

    private fun emptyAndFill(
        bottle: ItemLike,
        fluid: HTFluidContent,
        amount: Int = 250,
        container: ItemLike = Items.GLASS_BOTTLE,
    ) {
        // Emptying
        HTTankInteractionRecipeBuilder.emptying {
            ingredient = ingredientCreator.create(bottle)
            fluidResult = resultCreator.create(fluid, amount)
            itemResult = resultCreator.create(container)
        }.save(output)
        // Filling
        HTTankInteractionRecipeBuilder.filling {
            itemIngredient = ingredientCreator.create(container)
            fluidIngredient = fluidCreator.create(fluid, amount)
            itemResult = resultCreator.create(bottle)
        }.save(output)
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCTankInteractionRecipeProvider) {
        override fun getName(): String = "Tank Interaction Recipes"
    }
}
