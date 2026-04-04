package hiiragi283.core.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.recipe.withSize
import hiiragi283.core.common.data.recipe.builder.HCChargingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HCMeltingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTItemToChancedRecipeBuilder
import hiiragi283.core.impl.registry.VanillaFluidContents
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class HCRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(registries, output) {
    override fun buildRecipes() {
        charging()
        crushing()
        melting()
    }

    //    Charging    //

    private fun charging() {
        // Ender Pearl -> Ender Eye
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.ENDER_PEARLS)
            result += Items.ENDER_EYE
        }
        // Golden Apple
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.from(Items.GOLDEN_APPLE)
            result += Items.ENCHANTED_GOLDEN_APPLE
        }
        // Quartz -> Prismarine
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.GEMS_QUARTZ)
            result += Items.PRISMARINE_SHARD
        }
        // Redstone Dust -> Glowstone Dust
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.DUSTS_REDSTONE)
            result += Items.GLOWSTONE_DUST
        }
        // Honey Bottle -> Exp Bottle
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.DRINKS_HONEY)
            result += Items.EXPERIENCE_BOTTLE
        }

        // End Crystal -> Eldritch Pearl
        /*HCChargingRecipeBuilder.charging(output) {
            ingredient = itemCreator.from(Items.END_CRYSTAL)
            result += CommonParts.PEARL, HCMaterialKeys.ELDRITCH
        }
        // Heart of the Sea
        HCChargingRecipeBuilder.charging(output) {
            ingredient = itemCreator.from(HCItems.ELDER_HEART)
            result += Items.HEART_OF_THE_SEA
        }*/
        // Nether Star
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.NETHER_STARS)
            result += Items.NETHER_STAR
        }
    }

    //    Crushing    //

    private fun crushing() {
        mapOf(
            Items.NETHER_WART to Items.NETHER_WART_BLOCK,
            HCBlocks.WARPED_WART.asItem() to Items.WARPED_WART_BLOCK,
        ).forEach { (output: Item, input: Item) ->
            HTItemToChancedRecipeBuilder.crushing(this.output) {
                ingredient = itemCreator.fromItem(input) withSize 1
                result = ItemStackTemplate(output, 3)
            }
        }

        mapOf(
            Items.BRICK to Items.BRICKS,
            Items.NETHER_BRICK to Items.NETHER_BRICKS,
            Items.PRISMARINE_SHARD to Items.PRISMARINE,
            Items.SNOWBALL to Items.SNOW_BLOCK,
        ).forEach { (output: Item, input: Item) ->
            HTItemToChancedRecipeBuilder.crushing(this.output) {
                ingredient = itemCreator.fromItem(input) withSize 1
                result = ItemStackTemplate(output, 4)
                recipeId suffix "_from_block"
            }
        }

        // Prismarine Bricks -> Prismarine Shard
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = itemCreator.fromItem(Items.PRISMARINE_BRICKS) withSize 1
            result = ItemStackTemplate(Items.PRISMARINE_SHARD, 9)
            recipeId suffix "_from_bricks"
        }
        // Beetroot -> Sugar + Molasses
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.CROPS_BEETROOT) withSize 1
            result = ItemStackTemplate(Items.SUGAR, 2)
            // extraResult += ItemStackTemplate(RagiumItems.MOLASSES)
            recipeId suffix "_from_beetroot"
        }
        // Sugar Cane -> Sugar + Molasses
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.CROPS_SUGAR_CANE) withSize 1
            result = ItemStackTemplate(Items.SUGAR, 4)
            // extraResult += ItemStackTemplate(RagiumItems.MOLASSES)
            recipeId suffix "_from_cane"
        }
        // Ice -> Snowball
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = itemCreator.fromItem(Items.ICE) withSize 1
            result = ItemStackTemplate(Items.SNOWBALL, 4)
            recipeId suffix "_from_ice"
        }
        // Wheat -> Flour
        /*HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.CROPS_WHEAT) withSize 1
            result = ItemStackTemplate(HCItems.WHEAT_FLOUR)
        }*/

        crushStones()
    }

    private fun crushStones() {
        // Stone -> Cobblestone
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = itemCreator.fromItem(Items.STONE) withSize 1
            result = ItemStackTemplate(Items.COBBLESTONE)
            recipeId suffix "_from_stone"
        }
        // Cobblestone -> Gravel
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = itemCreator.fromTagKeys(listOf(Tags.Items.COBBLESTONES_NORMAL, Tags.Items.COBBLESTONES_MOSSY)) withSize 1
            result = ItemStackTemplate(Items.GRAVEL)
            recipeId suffix "_from_cobblestone"
        }
        // Gravel -> Sand
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.GRAVELS) withSize 1
            result = ItemStackTemplate(Items.SAND)
            recipeId suffix "_from_gravel"
        }
        // Sandstone -> Sand + Saltpeter
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS) withSize 1
            result = ItemStackTemplate(Items.SAND, 2)
            // extraResult += resultCreator.material(CommonParts.DUST, CommonMaterialKeys.SALTPETER) to fraction(1, 4)
            recipeId suffix "_from_sandstone"
        }

        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.SANDSTONE_RED_BLOCKS) withSize 1
            result = ItemStackTemplate(Items.RED_SAND, 2)
            // extraResult += resultCreator.material(CommonParts.DUST, CommonMaterialKeys.SALTPETER) to fraction(1, 4)
            recipeId suffix "_from_sandstone"
        }
    }

    //    Charging    //

    private fun melting() {
        // Water
        HCMeltingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromItem(Items.SNOWBALL)
            result = VanillaFluidContents.WATER.toTemplate(250)
            time = 25
            recipeId suffix "_from_snowball"
        }
        HCMeltingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromItem(Items.SNOW_BLOCK)
            result = VanillaFluidContents.WATER.toTemplate()
            time = 100
            recipeId suffix "_from_snow"
        }
        HCMeltingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromItem(Items.ICE)
            result = VanillaFluidContents.WATER.toTemplate()
            recipeId suffix "_from_ice"
        }
        // Lava
        HCMeltingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.COBBLESTONES)
            result = VanillaFluidContents.LAVA.toTemplate(100)
            time *= 3
            heatRange = heatRange(1000)
            recipeId suffix "_from_cobblestones"
        }

        // Honey
        HCMeltingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromItem(Items.HONEY_BLOCK)
            result = HCFluids.HONEY.toTemplate()
            heatRange = heatRange(60)
            recipeId suffix "_from_block"
        }
    }
}
