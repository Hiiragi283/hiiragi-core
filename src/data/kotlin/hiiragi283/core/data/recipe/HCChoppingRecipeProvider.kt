package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.data.recipe.HTRecipeProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

class HCChoppingRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipes() {
        // Sapling -> Stick
        HCRecipeBuilders.chopping {
            ingredient { +holderSet(ItemTags.SAPLINGS) }
            result { +Items.STICK }
            recipeId suffix "_from_saplings"
        }.save(exporter)
        // Slab -> Stick
        HCRecipeBuilders.chopping {
            ingredient { +holderSet(ItemTags.WOODEN_SLABS) }
            result {
                +Items.STICK
                count = 2
            }
            recipeId suffix "_from_wooden_slabs"
        }.save(exporter)
        // Book -> Paper + Leather
        HCRecipeBuilders.chopping {
            ingredient { items { +Items.BOOK } }
            result {
                +Items.PAPER
                count = 3
            }
            result { +Items.LEATHER }
            recipeId suffix "_from_book"
        }.save(exporter)
    }

    override fun getName(): String = "Chopping Recipes"
}
