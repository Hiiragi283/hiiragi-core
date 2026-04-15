package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTMinMaxRange
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HCExplodingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HCMeltingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTDoubleMultiOutputRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTSingleMultiOutputRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTTankInteractionRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

data object HCBasicRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        charging()
        crushing()
        exploding()
        forging()
        melting()

        tankInteraction()
    }

    //    Charging    //

    @JvmStatic
    private fun charging() {
        // Ender Pearl -> Ender Eye
        HTSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER)
            result = resultCreator.create(Items.ENDER_EYE)
        }
        // Golden Apple
        HTSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(Items.GOLDEN_APPLE)
            result = resultCreator.create(Items.ENCHANTED_GOLDEN_APPLE)
        }
        // Quartz -> Prismarine
        HTSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ)
            result = resultCreator.create(Items.PRISMARINE_SHARD)
        }
        // Redstone Dust -> Glowstone Dust
        HTSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            result = resultCreator.material(CommonParts.DUST, VanillaMaterialKeys.GLOWSTONE)
        }
        // Honey Bottle -> Exp Bottle
        HTSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(Tags.Items.DRINKS_HONEY)
            result = resultCreator.create(Items.EXPERIENCE_BOTTLE)
        }

        // End Crystal -> Eldritch Pearl
        HTSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(Items.END_CRYSTAL)
            result = resultCreator.material(CommonParts.PEARL, HCMaterialKeys.ELDRITCH)
        }
        // Heart of the Sea
        HTSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(HCItems.ELDER_HEART)
            result = resultCreator.create(Items.HEART_OF_THE_SEA)
        }
        // Nether Star
        HTSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(HCItems.WITHER_STAR)
            result = resultCreator.create(Items.NETHER_STAR)
        }
    }

    //    Crushing    //

    @JvmStatic
    private fun crushing() {
        mapOf(
            HCItems.LUMINOUS_PASTE to Items.GLOW_INK_SAC,
            HCItems.MAGMA_SHARD to Items.MAGMA_BLOCK,
        ).forEach { (output: ItemLike, input: Item) ->
            HTSingleMultiOutputRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(input)
                results += resultCreator.create(output)
            }
        }

        mapOf(
            Items.NETHER_WART to Items.NETHER_WART_BLOCK,
            HCBlocks.WARPED_WART to Items.WARPED_WART_BLOCK,
        ).forEach { (output: ItemLike, input: Item) ->
            HTSingleMultiOutputRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(input)
                results += resultCreator.create(output, 3)
            }
        }

        mapOf(
            Items.BRICK to Items.BRICKS,
            Items.NETHER_BRICK to Items.NETHER_BRICKS,
            Items.PRISMARINE_SHARD to Items.PRISMARINE,
            Items.SNOWBALL to Items.SNOW_BLOCK,
        ).forEach { (output: Item, input: Item) ->
            HTSingleMultiOutputRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(input)
                results += resultCreator.create(output, 4)
                recipeId suffix "_from_block"
            }
        }

        // Prismarine Bricks -> Prismarine Shard
        HTSingleMultiOutputRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.PRISMARINE_BRICKS)
            results += resultCreator.create(Items.PRISMARINE_SHARD, 9)
            recipeId suffix "_from_bricks"
        }
        // Beetroot -> Sugar + Molasses
        HTSingleMultiOutputRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_BEETROOT)
            results += resultCreator.create(Items.SUGAR, 2)
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_beetroot"
        }
        // Sugar Cane -> Sugar + Molasses
        HTSingleMultiOutputRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_SUGAR_CANE)
            results += resultCreator.create(Items.SUGAR, 4)
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_cane"
        }
        // Ice -> Snowball
        HTSingleMultiOutputRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.ICE)
            results += resultCreator.create(Items.SNOWBALL, 4)
            recipeId suffix "_from_ice"
        }
        // Wheat -> Flour
        HTSingleMultiOutputRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_WHEAT)
            results += resultCreator.create(HCItems.WHEAT_FLOUR)
        }

        crushStones()
        crushWoods()
    }

    @JvmStatic
    private fun crushStones() {
        // Stone -> Cobblestone
        HTSingleMultiOutputRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.STONE)
            results += resultCreator.create(Items.COBBLESTONE)
            recipeId suffix "_from_stone"
        }
        // Cobblestone -> Gravel
        HTSingleMultiOutputRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.COBBLESTONES_NORMAL, Tags.Items.COBBLESTONES_MOSSY))
            results += resultCreator.create(Items.GRAVEL)
            recipeId suffix "_from_cobblestone"
        }
        // Gravel -> Sand
        HTSingleMultiOutputRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.GRAVELS)
            results += resultCreator.create(Items.SAND)
            recipeId suffix "_from_gravel"
        }
        // Sandstone -> Sand + Saltpeter
        HTSingleMultiOutputRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            results += resultCreator.create(Items.SAND, 2)
            results += resultCreator.material(CommonParts.DUST, CommonMaterialKeys.SALTPETER, chance = fraction(1, 4))
            recipeId suffix "_from_sandstone"
        }

        HTSingleMultiOutputRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.SANDSTONE_RED_BLOCKS)
            results += resultCreator.create(Items.RED_SAND, 2)
            results += resultCreator.material(CommonParts.DUST, CommonMaterialKeys.SALTPETER, chance = fraction(1, 4))
            recipeId suffix "_from_sandstone"
        }
    }

    @JvmStatic
    private fun crushWoods() {
        // Wood Dust
        fun wood(tagKey: TagKey<Item>, input: Int, output: Int) {
            HTSingleMultiOutputRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(tagKey, input)
                results += resultCreator.material(CommonParts.DUST, VanillaMaterialKeys.WOOD, output)
                recipeId suffix "_from_${tagKey.location().path}"
            }
        }

        wood(ItemTags.BOATS, 1, 5)
        wood(ItemTags.LOGS_THAT_BURN, 1, 6)
        wood(ItemTags.WOODEN_BUTTONS, 1, 1)
        wood(ItemTags.WOODEN_DOORS, 1, 2)
        wood(ItemTags.WOODEN_PRESSURE_PLATES, 1, 2)
        wood(ItemTags.WOODEN_SLABS, 2, 1)
        wood(ItemTags.WOODEN_STAIRS, 4, 3)
        wood(ItemTags.WOODEN_TRAPDOORS, 1, 3)
        wood(Tags.Items.BARRELS_WOODEN, 1, 7)
        wood(Tags.Items.CHESTS_WOODEN, 1, 8)
        wood(Tags.Items.FENCE_GATES_WOODEN, 1, 4)
        wood(Tags.Items.FENCES_WOODEN, 1, 5)
        wood(Tags.Items.RODS_WOODEN, 2, 1)
    }

    //    Exploding    //

    @JvmStatic
    private fun exploding() {
        // Cobblestone -> Cobbled Deepslate
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.STONES, Tags.Items.COBBLESTONES_NORMAL), amount = 2)
            result = resultCreator.create(Items.COBBLED_DEEPSLATE)
            minPower = fraction(3f)
        }
        // Ancient Debris -> Netherite Scrap
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.ORES_NETHERITE_SCRAP)
            result = resultCreator.material(CommonParts.SCRAP, VanillaMaterialKeys.NETHERITE, 2)
            minPower = fraction(6f)
        }
        // Gunpowder -> Blaze Powder
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.GUNPOWDERS, 3)
            result = resultCreator.create(Items.BLAZE_POWDER)
        }
        // Glass -> Quartz
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.GLASS_BLOCKS, 4)
            result = resultCreator.material(CommonParts.GEM, VanillaMaterialKeys.QUARTZ)
        }
        // Quartz Block -> Ghast Tear
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ, 4)
            result = resultCreator.create(Items.GHAST_TEAR)
            minPower = fraction(3f)
        }

        gems()
    }

    @JvmStatic
    private fun gems() {
        // Diamond
        mapOf(
            listOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL) to 64,
            listOf(CommonMaterialKeys.COAL_COKE) to 32,
        ).forEach { (fuels: List<HTMaterialKey>, count: Int) ->
            HCExplodingRecipeBuilder.create(output) {
                ingredient = inputCreator.create(fuels.flatMap(::baseOrDust), count)
                result = resultCreator.material(CommonParts.GEM, VanillaMaterialKeys.DIAMOND)
                recipeId suffix "_from_${fuels.joinToString(separator = "_or_", transform = HTMaterialKey::path)}"
            }
        }

        // Echo Shard
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.SCULK, 8)
            result = resultCreator.material(CommonParts.GEM, VanillaMaterialKeys.ECHO)
            minPower = fraction(6f)
        }

        // Crimson Crystal
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.CRIMSON_STEMS, 12)
            result = resultCreator.material(CommonParts.GEM, HCMaterialKeys.CRIMSON_CRYSTAL)
        }
        // Warped Crystal
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.WARPED_STEMS, 12)
            result = resultCreator.material(CommonParts.GEM, HCMaterialKeys.WARPED_CRYSTAL)
        }
    }

    //    Forging    //

    @JvmStatic
    private fun forging() {
        HTDoubleMultiOutputRecipeBuilder.forging(output) {
            base = inputCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
            addition = inputCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
            results += resultCreator.create(Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
        }
    }

    //    Melting    //

    @JvmStatic
    private fun melting() {
        val iceRange: HTMinMaxRange<Int> = HTMinMaxRange.atLeast(HTConst.STANDARD_TEMP)
        // Water
        HCMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.SNOWBALL)
            result = resultCreator.water(250)
            heatRange = iceRange
            time = 25
            recipeId suffix "_from_snowball"
        }
        HCMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.SNOW_BLOCK)
            result = resultCreator.water()
            heatRange = iceRange
            time = 100
            recipeId suffix "_from_snow"
        }
        HCMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.ICE)
            result = resultCreator.water()
            heatRange = iceRange
            recipeId suffix "_from_ice"
        }
    }

    //    Tank Interaction    //

    @JvmStatic
    private fun tankInteraction() {
        emptyAndFill(Items.EXPERIENCE_BOTTLE, HCFluids.EXPERIENCE)
        emptyAndFill(Items.HONEY_BOTTLE, HCFluids.HONEY)
        emptyAndFill(Items.MUSHROOM_STEW, HCFluids.MUSHROOM_STEW, container = Items.BOWL)
        emptyAndFill(Items.DRAGON_BREATH, HCFluids.DRAGON_BREATH)

        emptyAndFill(Items.WET_SPONGE, VanillaFluidContents.WATER, HTConst.DEFAULT_FLUID_AMOUNT, Items.SPONGE)

        repeat(5) { amplifier: Int ->
            HTTankInteractionRecipeBuilder.emptying(output) {
                ingredient = inputCreator.create(false, Items.OMINOUS_BOTTLE) {
                    expect(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, amplifier)
                }
                fluidResult = resultCreator.create(HCFluids.OMINOUS_FLUX, 250 * (amplifier + 1))
                itemResult = resultCreator.create(Items.GLASS_BOTTLE)
                recipeId suffix "_$amplifier"
            }
        }
    }

    @JvmStatic
    private fun emptyAndFill(
        bottle: ItemLike,
        fluid: HTFluidContent,
        amount: Int = 250,
        container: ItemLike = Items.GLASS_BOTTLE,
    ) {
        // Emptying
        HTTankInteractionRecipeBuilder.emptying(output) {
            ingredient = inputCreator.create(bottle)
            fluidResult = resultCreator.create(fluid, amount)
            itemResult = resultCreator.create(container)
        }
        // Filling
        HTTankInteractionRecipeBuilder.filling(output) {
            itemIngredient = inputCreator.create(container)
            fluidIngredient = inputCreator.create(fluid, amount)
            itemResult = resultCreator.create(bottle)
        }
    }
}
