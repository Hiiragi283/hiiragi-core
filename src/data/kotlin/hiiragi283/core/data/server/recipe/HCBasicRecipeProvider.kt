package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HCExplodingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTItemToChancedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTItemToItemRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
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
    }

    //    Charging    //

    @JvmStatic
    private fun charging() {
        // Ender Pearl -> Ender Eye
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER)
            result = resultCreator.create(Items.ENDER_EYE)
        }
        // Golden Apple
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(Items.GOLDEN_APPLE)
            result = resultCreator.create(Items.ENCHANTED_GOLDEN_APPLE)
        }
        // Quartz -> Prismarine
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ)
            result = resultCreator.create(Items.PRISMARINE_SHARD)
        }
        // Redstone Dust -> Glowstone Dust
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE)
        }

        // End Crystal -> Eldritch Pearl
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(Items.END_CRYSTAL)
            result = resultCreator.material(CommonTagPrefixes.PEARL, HCMaterialKeys.ELDRITCH)
        }
        // Heart of the Sea
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(HCItems.ELDER_HEART)
            result = resultCreator.create(Items.HEART_OF_THE_SEA)
        }
        // Nether Star
        HTItemToItemRecipeBuilder.charging(output) {
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
            HTItemToChancedRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(input)
                result = resultCreator.create(output)
            }
        }

        mapOf(
            Items.NETHER_WART to Items.NETHER_WART_BLOCK,
            HCBlocks.WARPED_WART to Items.WARPED_WART_BLOCK,
        ).forEach { (output: ItemLike, input: Item) ->
            HTItemToChancedRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(input)
                result = resultCreator.create(output, 3)
            }
        }

        mapOf(
            HTSimpleDeferredItem(CommonTagPrefixes.DUST.createId(VanillaMaterialKeys.WOOD)) to ItemTags.LOGS,
            Items.SAND to Tags.Items.SANDSTONE_UNCOLORED_BLOCKS,
            Items.RED_SAND to Tags.Items.SANDSTONE_RED_BLOCKS,
        ).forEach { (output: ItemLike, input: TagKey<Item>) ->
            HTItemToChancedRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(input)
                result = resultCreator.create(output, 4)
                recipeId suffix "_from_block"
            }
        }

        mapOf(
            Items.BRICK to Items.BRICKS,
            Items.NETHER_BRICK to Items.NETHER_BRICKS,
            Items.PRISMARINE_SHARD to Items.PRISMARINE,
            Items.SNOWBALL to Items.SNOW_BLOCK,
        ).forEach { (output: Item, input: Item) ->
            HTItemToChancedRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(input)
                result = resultCreator.create(output, 4)
                recipeId suffix "_from_block"
            }
        }

        // Prismarine Bricks -> Prismarine Shard
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.PRISMARINE_BRICKS)
            result = resultCreator.create(Items.PRISMARINE_SHARD, 9)
            recipeId suffix "_from_bricks"
        }
        // Beetroot -> Sugar + Molasses
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_BEETROOT)
            result = resultCreator.create(Items.SUGAR, 2)
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_beetroot"
        }
        // Sugar Cane -> Sugar + Molasses
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_SUGAR_CANE)
            result = resultCreator.create(Items.SUGAR, 4)
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_cane"
        }
        // Ice -> Snowball
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.ICE)
            result = resultCreator.create(Items.SNOWBALL, 4)
            recipeId suffix "_from_ice"
        }

        crushStones()
        crushWoods()
    }

    @JvmStatic
    private fun crushStones() {
        // Stone -> Cobblestone
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.STONE)
            result = resultCreator.create(Items.COBBLESTONE)
            recipeId suffix "_from_stone"
        }
        // Cobblestone -> Gravel
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.COBBLESTONES_NORMAL, Tags.Items.COBBLESTONES_MOSSY))
            result = resultCreator.create(Items.GRAVEL)
            recipeId suffix "_from_cobblestone"
        }
        // Gravel -> Sand
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.GRAVELS)
            result = resultCreator.create(Items.SAND)
            recipeId suffix "_from_gravel"
        }
        // Sandstone -> Sand + Saltpeter
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            result = resultCreator.create(Items.SAND, 2)
            extraResult += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER) to fraction(1, 4)
            recipeId suffix "_from_sandstone"
        }

        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.SANDSTONE_RED_BLOCKS)
            result = resultCreator.create(Items.RED_SAND, 2)
            extraResult += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER) to fraction(1, 4)
            recipeId suffix "_from_sandstone"
        }
    }

    @JvmStatic
    private fun crushWoods() {
        // Wood Dust
        fun wood(tagKey: TagKey<Item>, input: Int, output: Int) {
            HTItemToChancedRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(tagKey, input)
                result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD, output)
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
            result = resultCreator.material(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2)
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
            result = resultCreator.material(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ)
        }
        // Quartz Block -> Ghast Tear
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.QUARTZ, 4)
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
                result = resultCreator.material(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
                recipeId suffix "_from_${fuels.joinToString(separator = "_or_", transform = HTMaterialKey::path)}"
            }
        }

        // Echo Shard
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.SCULK, 8)
            result = resultCreator.material(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO)
            minPower = fraction(6f)
        }

        // Crimson Crystal
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.CRIMSON_STEMS, 12)
            result = resultCreator.material(CommonTagPrefixes.GEM, HCMaterialKeys.CRIMSON_CRYSTAL)
        }
        // Warped Crystal
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.WARPED_STEMS, 12)
            result = resultCreator.material(CommonTagPrefixes.GEM, HCMaterialKeys.WARPED_CRYSTAL)
        }
    }
}
