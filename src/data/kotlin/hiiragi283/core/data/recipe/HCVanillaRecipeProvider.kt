package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.recipe.custom.HCEternalSmithingRecipe
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.recipe.RecipeKey
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.CustomCraftingRecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
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
        // Chopping Board
        shaped(RecipeCategory.MISC, HCBlocks.CHOPPING_BOARD)
            .pattern("A")
            .pattern("B")
            .define('A', ItemTags.WOODEN_SLABS)
            .define('B', Tags.Items.STRIPPED_LOGS)
            .unlockedBy("has_axe", has(ItemTags.AXES))
            .save(output)
        // Copper Basin
        shaped(RecipeCategory.MISC, HCBlocks.COPPER_BASIN.weathering.unaffected)
            .pattern("A A")
            .pattern("A A")
            .pattern("BAB")
            .define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.STORAGE_BLOCKS_COPPER)
            .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
            .save(output)

        for ((state: WeatheringCopper.WeatherState, base: ItemLike) in HCBlocks.COPPER_BASIN.weathering) {
            val waxed: ItemLike = HCBlocks.COPPER_BASIN.waxed[state]
            // Waxing
            shapeless(RecipeCategory.MISC, waxed)
                .requires(base)
                .requires(Items.HONEYCOMB)
                .group("copper_basin")
                .unlockedBy(getHasName(base), has(base))
                .save(output, getConversionRecipeName(waxed, Items.HONEYCOMB))
        }

        // Eternal Upgrade
        shaped(RecipeCategory.MISC, HCItems.ETERNAL_UPGRADE, 2)
            .pattern("ABA")
            .pattern("ACA")
            .pattern("AAA")
            .define('A', Tags.Items.GEMS_DIAMOND)
            .define('B', HCItems.ETERNAL_UPGRADE)
            .define('C', HCItems.IRIDESCENT_POWDER)
            .unlockedBy(getHasName(HCItems.ETERNAL_UPGRADE), has(HCItems.ETERNAL_UPGRADE))
            .save(output)

        CustomCraftingRecipeBuilder.customCrafting(RecipeCategory.MISC) { _, _ -> HCEternalSmithingRecipe }
            .unlockedBy(getHasName(HCItems.ETERNAL_UPGRADE), has(HCItems.ETERNAL_UPGRADE))
            .save(output, RecipeKey(modId, "${HTConstants.SMITHING}/eternal_upgrade"))
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCVanillaRecipeProvider) {
        override fun getName(): String = "Vanilla Recipes"
    }
}
