package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.data.recipe.HTRecipeProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

class HCChoppingRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {
        // Sapling -> Stick
        HCRecipeBuilders.chopping {
            ingredient { +holderSet(ItemTags.SAPLINGS) }
            result { +Items.STICK }
            recipeId suffix "_from_saplings"
        }.save(output)
        // Slab -> Stick
        HCRecipeBuilders.chopping {
            ingredient { +holderSet(ItemTags.WOODEN_SLABS) }
            result {
                +Items.STICK
                count = 2
            }
            recipeId suffix "_from_wooden_slabs"
        }.save(output)
        // Book -> Paper + Leather
        HCRecipeBuilders.chopping {
            ingredient { items { +Items.BOOK } }
            result {
                +Items.PAPER
                count = 3
            }
            result { +Items.LEATHER }
            recipeId suffix "_from_book"
        }.save(output)
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCChoppingRecipeProvider) {
        override fun getName(): String = "Chopping Recipes"
    }
}
