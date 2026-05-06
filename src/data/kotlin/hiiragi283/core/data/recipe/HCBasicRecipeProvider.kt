package hiiragi283.core.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HCChargingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HCExplodingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTItemToMultiItemRecipeBuilder
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

        tankInteraction()
    }

    //    Charging    //

    @JvmStatic
    private fun charging() {
        // Ender Pearl -> Ender Eye
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER)
            result = resultCreator.create(Items.ENDER_EYE, chance = fraction(1, 2))
        }
        // Golden Apple
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(Items.GOLDEN_APPLE)
            result = resultCreator.create(Items.ENCHANTED_GOLDEN_APPLE, chance = fraction(1, 8))
        }
        // Quartz -> Prismarine
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ)
            result = resultCreator.create(Items.PRISMARINE_SHARD, chance = fraction(3, 4))
        }
        // Redstone Dust -> Glowstone Dust
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            result = resultCreator.material(CommonParts.DUST, VanillaMaterialKeys.GLOWSTONE, chance = fraction(3, 4))
        }
        // Honey Bottle -> Exp Bottle
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(Tags.Items.DRINKS_HONEY)
            result = resultCreator.create(Items.EXPERIENCE_BOTTLE, chance = fraction(1, 2))
        }

        // End Crystal -> Eldritch Pearl
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(Items.END_CRYSTAL)
            result = resultCreator.material(CommonParts.PEARL, HCMaterialKeys.ELDRITCH, chance = fraction(1, 4))
        }
        // Heart of the Sea
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(HCItems.ELDER_HEART)
            result = resultCreator.create(Items.HEART_OF_THE_SEA)
        }
        // Nether Star
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(HCItems.WITHER_STAR)
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
            HTItemToMultiItemRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(input)
                results += resultCreator.create(output)
            }
        }

        mapOf(
            Items.NETHER_WART to Items.NETHER_WART_BLOCK,
            HCBlocks.WARPED_WART to Items.WARPED_WART_BLOCK,
        ).forEach { (output: ItemLike, input: Item) ->
            HTItemToMultiItemRecipeBuilder.crushing(this.output) {
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
            HTItemToMultiItemRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(input)
                results += resultCreator.create(output, 4)
                recipeId suffix "_from_block"
            }
        }

        // Prismarine Bricks -> Prismarine Shard
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.PRISMARINE_BRICKS)
            results += resultCreator.create(Items.PRISMARINE_SHARD, 9)
            recipeId suffix "_from_bricks"
        }
        // Beetroot -> Sugar + Molasses
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_BEETROOT)
            results += resultCreator.create(Items.SUGAR, 2)
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_beetroot"
        }
        // Sugar Cane -> Sugar + Molasses
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_SUGAR_CANE)
            results += resultCreator.create(Items.SUGAR, 4)
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_cane"
        }
        // Ice -> Snowball
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.ICE)
            results += resultCreator.create(Items.SNOWBALL, 4)
            recipeId suffix "_from_ice"
        }
        // Wheat -> Flour
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_WHEAT)
            results += resultCreator.create(HCItems.WHEAT_FLOUR)
        }

        crushStones()
        crushWoods()
    }

    @JvmStatic
    private fun crushStones() {
        // Stone -> Cobblestone
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.STONE)
            results += resultCreator.create(Items.COBBLESTONE)
            recipeId suffix "_from_stone"
        }
        // Cobblestone -> Gravel
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.COBBLESTONES_NORMAL, Tags.Items.COBBLESTONES_MOSSY))
            results += resultCreator.create(Items.GRAVEL)
            recipeId suffix "_from_cobblestone"
        }
        // Gravel -> Sand
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.GRAVELS)
            results += resultCreator.create(Items.SAND)
            recipeId suffix "_from_gravel"
        }
        // Sandstone -> Sand + Saltpeter
        HTItemToMultiItemRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            results += resultCreator.create(Items.SAND, 2)
            results += resultCreator.material(CommonParts.DUST, CommonMaterialKeys.SALTPETER, chance = fraction(1, 4))
            recipeId suffix "_from_sandstone"
        }

        HTItemToMultiItemRecipeBuilder.crushing(output) {
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
            HTItemToMultiItemRecipeBuilder.crushing(this.output) {
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
            ingredient = itemCreator.create(listOf(Tags.Items.STONES, Tags.Items.COBBLESTONES_NORMAL))
            result = resultCreator.create(Items.COBBLED_DEEPSLATE, chance = fraction(1, 2))
        }
        // Ancient Debris -> Netherite Scrap
        HCExplodingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(Tags.Items.ORES_NETHERITE_SCRAP)
            result = resultCreator.material(CommonParts.SCRAP, VanillaMaterialKeys.NETHERITE, 2)
        }
        // Gunpowder -> Blaze Powder
        HCExplodingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(Tags.Items.GUNPOWDERS)
            result = resultCreator.create(Items.BLAZE_POWDER, chance = fraction(1, 6))
        }
        // Glass -> Quartz
        HCExplodingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(Tags.Items.GLASS_BLOCKS)
            result = resultCreator.material(CommonParts.GEM, VanillaMaterialKeys.QUARTZ, chance = fraction(1, 4))
        }
        // Quartz Block -> Ghast Tear
        HCExplodingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ)
            result = resultCreator.create(Items.GHAST_TEAR, chance = fraction(1, 4))
        }

        gems()
    }

    @JvmStatic
    private fun gems() {
        // Diamond
        mapOf(
            listOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL) to 64,
            listOf(CommonMaterialKeys.COAL_COKE) to 32,
            listOf(CommonMaterialKeys.CARBON) to 16,
        ).forEach { (fuels: List<HTMaterialKey>, count: Int) ->
            HCExplodingRecipeBuilder.create(output) {
                ingredient = itemCreator.create(fuels.flatMap(::baseOrDust))
                result = resultCreator.material(CommonParts.GEM, VanillaMaterialKeys.DIAMOND, chance = fraction(1, count))
                recipeId suffix "_from_${fuels.joinToString(separator = "_or_", transform = HTMaterialKey::path)}"
            }
        }

        // Echo Shard
        HCExplodingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(Items.SCULK)
            result = resultCreator.material(CommonParts.GEM, VanillaMaterialKeys.ECHO, chance = fraction(1, 8))
        }

        // Crimson Crystal
        HCExplodingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(ItemTags.CRIMSON_STEMS)
            result = resultCreator.material(CommonParts.GEM, HCMaterialKeys.CRIMSON_CRYSTAL, chance = fraction(1, 8))
        }
        // Warped Crystal
        HCExplodingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(ItemTags.WARPED_STEMS)
            result = resultCreator.material(CommonParts.GEM, HCMaterialKeys.WARPED_CRYSTAL, chance = fraction(1, 8))
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
                ingredient = itemCreator.create(false, Items.OMINOUS_BOTTLE) {
                    expect(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, amplifier)
                }
                fluidResult = resultCreator.create(HCFluids.OMINOUS_FLUX, 250 * (amplifier + 1))
                itemResult = resultCreator.create(Items.GLASS_BOTTLE)
                recipeId suffix "_$amplifier"
            }
        }

        // Honey Block <-> Honey
        HTTankInteractionRecipeBuilder.emptying(output) {
            ingredient = itemCreator.create(Items.HONEY_BLOCK)
            fluidResult = resultCreator.create(HCFluids.HONEY)
            recipeId suffix "_from_block"
        }
        HTTankInteractionRecipeBuilder.filling(output) {
            itemIngredient = itemCreator.create(Tags.Items.GLASS_BLOCKS)
            fluidIngredient = inputCreator.create(HCFluids.HONEY)
            itemResult = resultCreator.create(Items.HONEY_BLOCK)
        }

        // Latex + Bowl -> Raw Rubber
        HTTankInteractionRecipeBuilder.filling(output) {
            itemIngredient = itemCreator.create(Items.BOWL)
            fluidIngredient = inputCreator.create(HCFluids.LATEX)
            itemResult = resultCreator.create(HCItems.RAW_RUBBER, 4)
        }
        // Raw Rubber -> Rubber Bar
        HTCookingRecipeBuilder.smelting(output) {
            ingredient += HCItems.RAW_RUBBER
            resultStack += HCItems.CURED_RUBBER
            exp = 0.7f
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
            ingredient = itemCreator.create(bottle)
            fluidResult = resultCreator.create(fluid, amount)
            itemResult = resultCreator.create(container)
        }
        // Filling
        HTTankInteractionRecipeBuilder.filling(output) {
            itemIngredient = itemCreator.create(container)
            fluidIngredient = inputCreator.create(fluid, amount)
            itemResult = resultCreator.create(bottle)
        }
    }
}
