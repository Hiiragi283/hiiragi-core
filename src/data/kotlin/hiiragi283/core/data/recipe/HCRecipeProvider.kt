package hiiragi283.core.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.common.data.recipe.builder.HCChargingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HCMeltingRecipeBuilder
import hiiragi283.core.impl.registry.VanillaFluidContents
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class HCRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(registries, output) {
    override fun buildRecipes() {
        charging()
        melting()
    }

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
