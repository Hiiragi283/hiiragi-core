package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.recipe.custom.HCEternalSmithingRecipe
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import hiiragi283.lib.HTConstants
import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.color.VanillaColoredCollections
import hiiragi283.lib.copper.HTCopperPhase
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.HTShapedRecipeBuilder
import hiiragi283.lib.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.registry.HTDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.tag.CommonTagPrefixes
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class HCVanillaRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipes() {
        // Concrete Stairs
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            val concrete: HTSimpleDeferredBlockAndItem = VanillaColoredCollections.CONCRETE[color]
            // Slab
            HTShapedRecipeBuilder.create {
                +"AAA"
                define('A') { items { +concrete } }
                result {
                    +HCBlocks.CONCRETE_SLABS[color]
                    count = 6
                }
                category = RecipeCategory.BUILDING_BLOCKS
            }.save(exporter)
            // Stairs
            HTShapedRecipeBuilder.create {
                +"A  "
                +"AA "
                +"AAA"
                define('A') { items { +concrete } }
                result {
                    +HCBlocks.CONCRETE_STAIRS[color]
                    count = 4
                }
                category = RecipeCategory.BUILDING_BLOCKS
            }.save(exporter)
        }

        // Warped Wart
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"AAA"
            +"AAA"
            define('A') { items { +HCBlocks.WARPED_WART } }
            result { +Items.WARPED_WART_BLOCK }
            category = RecipeCategory.BUILDING_BLOCKS
        }.save(exporter)

        // Chopping Board
        HTShapedRecipeBuilder.create {
            +"A"
            +"B"
            define('A') { +holderSet(ItemTags.WOODEN_SLABS) }
            define('B') { +holderSet(Tags.Items.STRIPPED_LOGS) }
            result { +HCBlocks.CHOPPING_BOARD }
        }.save(exporter)
        // Forging Anvil
        HTShapedRecipeBuilder.create {
            +"A"
            +"B"
            define('A') { items { +Items.STONE_SLAB } }
            define('B') { items { +Items.SMOOTH_STONE } }
            result { +HCBlocks.FORGING_ANVIL }
        }.save(exporter)
        // Copper Basin
        HTShapedRecipeBuilder.create {
            +"A A"
            +"A A"
            +"BAB"
            define('A') { +holderSet(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER) }
            define('B') { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.COPPER) }
            result { +HCBlocks.COPPER_BASIN.weathering.unaffected }
        }.save(exporter)

        for ((phase: HTCopperPhase, base: HTDeferredBlockAndItem<*, *>) in HCBlocks.COPPER_BASIN.weathering.asSequenceWithPhase()) {
            val waxed: HTDeferredBlockAndItem<*, *> = HCBlocks.COPPER_BASIN.waxed[phase]
            // Waxing
            HTShapelessRecipeBuilder.create {
                ingredient { items { +base } }
                ingredient { items { +Items.HONEYCOMB } }
                result { +waxed }
                group = "copper_basin"
                recipeId replace waxed.getId().withSuffix("_from_honeycomb")
            }.save(exporter)
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
        }.save(exporter)

        exporter.accept(id(HTConstants.SMITHING, "eternal_upgrade"), HCEternalSmithingRecipe)
    }

    override fun getName(): String = "Vanilla Recipes"
}
