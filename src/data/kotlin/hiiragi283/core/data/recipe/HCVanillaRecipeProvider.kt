package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.lib.data.recipe.HTRecipeProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.WeatheringCopper
import net.neoforged.neoforge.common.Tags

class HCVanillaRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {
        // Warped Wart
        shaped(RecipeCategory.BUILDING_BLOCKS, Items.WARPED_WART_BLOCK)
            .pattern("AAA")
            .pattern("AAA")
            .pattern("AAA")
            .define('A', HCBlocks.WARPED_WART)
            .unlockedBy(getHasName(HCBlocks.WARPED_WART), has(HCBlocks.WARPED_WART))
            .save(output)
        // Copper Basin
        shaped(RecipeCategory.BUILDING_BLOCKS, HCBlocks.COPPER_BASIN.unaffected)
            .pattern("A A")
            .pattern("A A")
            .pattern("BAB")
            .define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.STORAGE_BLOCKS_COPPER)
            .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
            .save(output)

        for ((state: WeatheringCopper.WeatherState, base: ItemLike) in HCBlocks.COPPER_BASIN.weatheringMap) {
            val waxed: ItemLike = HCBlocks.COPPER_BASIN.waxedMap[state]!!
            // Waxing
            shapeless(RecipeCategory.BUILDING_BLOCKS, waxed)
                .requires(base)
                .requires(Items.HONEYCOMB)
                .group("copper_basin")
                .unlockedBy(getHasName(base), has(base))
                .save(output, getConversionRecipeName(waxed, Items.HONEYCOMB))
        }
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCVanillaRecipeProvider) {
        override fun getName(): String = "Vanilla Recipes"
    }
}
