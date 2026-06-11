package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.recipe.custom.HCEternalSmithingRecipe
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.HTShapedRecipeBuilder
import hiiragi283.lib.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.registry.HTDeferredBlockAndItem
import hiiragi283.lib.tag.CommonTagPrefixes
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.CustomCraftingRecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.WeatheringCopper
import net.neoforged.neoforge.common.Tags

class HCVanillaRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {
        // Warped Wart
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"AAA"
            +"AAA"
            define('A') { items { +HCBlocks.WARPED_WART.itemHolder } }
            result { +Items.WARPED_WART_BLOCK }
            category = RecipeCategory.BUILDING_BLOCKS
        }.save(output)
        // Chopping Board
        HTShapedRecipeBuilder.create {
            +"A"
            +"B"
            define('A') { +holderSet(ItemTags.WOODEN_SLABS) }
            define('B') { +holderSet(Tags.Items.STRIPPED_LOGS) }
            result { +HCBlocks.CHOPPING_BOARD.itemHolder }
        }.save(output)
        // Forging Anvil
        HTShapedRecipeBuilder.create {
            +"A"
            +"B"
            define('A') { items { +Items.STONE_SLAB } }
            define('B') { items { +Items.SMOOTH_STONE } }
            result { +HCBlocks.FORGING_ANVIL.itemHolder }
        }.save(output)
        // Copper Basin
        HTShapedRecipeBuilder.create {
            +"A A"
            +"A A"
            +"BAB"
            define('A') { +holderSet(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER) }
            define('B') { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.COPPER) }
            result { +HCBlocks.COPPER_BASIN.weathering.unaffected.itemHolder }
        }.save(output)

        for ((state: WeatheringCopper.WeatherState, base: HTDeferredBlockAndItem<*, *>) in HCBlocks.COPPER_BASIN.weathering) {
            val waxed: HTDeferredBlockAndItem<*, *> = HCBlocks.COPPER_BASIN.waxed[state]
            // Waxing
            HTShapelessRecipeBuilder.create {
                ingredient { items { +base.itemHolder } }
                ingredient { items { +Items.HONEYCOMB } }
                result { +waxed.itemHolder }
                group = "copper_basin"
                recipeId replace waxed.getId().withSuffix("_from_honeycomb")
            }.save(output)
        }

        // Eternal Upgrade
        HTShapedRecipeBuilder.create {
            +"ABA"
            +"ACA"
            +"AAA"
            define('A') { +holderSet(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND) }
            define('B') { items { +HCItems.ETERNAL_UPGRADE } }
            define('C') { items { +HCItems.IRIDESCENT_POWDER } }
            result {
                +HCItems.ETERNAL_UPGRADE
                count = 2
            }
        }.save(output)

        CustomCraftingRecipeBuilder.customCrafting(RecipeCategory.MISC) { _, _ -> HCEternalSmithingRecipe }
            .unlockedBy(getHasName(HCItems.ETERNAL_UPGRADE), has(HCItems.ETERNAL_UPGRADE))
            .save(output, RecipeKey(modId, HTConstants.SMITHING, "eternal_upgrade"))
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCVanillaRecipeProvider) {
        override fun getName(): String = "Vanilla Recipes"
    }
}
