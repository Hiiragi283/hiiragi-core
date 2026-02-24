package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HTItemToChancedRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
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
        crushing()
    }

    //    Crushing    //

    @JvmStatic
    private fun crushing() {
        mapOf(
            HCItems.LUMINOUS_PASTE to Items.GLOW_INK_SAC,
            HCItems.MAGMA_SHARD to Items.MAGMA_BLOCK,
        ).forEach { (output: ItemLike, input: ItemLike) ->
            HTItemToChancedRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(input)
                result = resultCreator.create(output)
            }
        }

        mapOf(
            Items.NETHER_WART to Items.NETHER_WART_BLOCK,
            HCBlocks.WARPED_WART to Items.WARPED_WART_BLOCK,
        ).forEach { (output: ItemLike, input: ItemLike) ->
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

        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.BRICKS)
            result = resultCreator.create(Items.BRICK, 4)
            recipeId suffix "_from_bricks"
        }

        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.PRISMARINE)
            result = resultCreator.create(Items.PRISMARINE_SHARD, 4)
        }

        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.PRISMARINE_BRICKS)
            result = resultCreator.create(Items.PRISMARINE_SHARD, 9)
            recipeId suffix "_from_bricks"
        }

        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.COBBLESTONES)
            result = resultCreator.create(Items.GRAVEL)
        }

        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.GRAVELS)
            result = resultCreator.create(Items.SAND)
        }

        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.SNOW_BLOCK)
            result = resultCreator.create(Items.SNOWBALL, 4)
            recipeId suffix "_from_block"
        }
        // Netherite Scrap
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.ORES_NETHERITE_SCRAP)
            result = resultCreator.material(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2)
            recipeId suffix "_from_ore"
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
}
