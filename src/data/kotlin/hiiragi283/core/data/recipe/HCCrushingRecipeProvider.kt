package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.material.CommonMaterialKeys
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.math.fraction
import hiiragi283.lib.recipe.result.HTItemResult
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

class HCCrushingRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {
        mapOf(
            Items.NETHER_WART to Items.NETHER_WART_BLOCK,
            HCBlocks.WARPED_WART to Items.WARPED_WART_BLOCK,
        ).forEach { (output: ItemLike, input: Item) ->
            HCRecipeBuilders.crushing {
                ingredient = itemCreator.create(input)
                result { +(output to 3) }
            }.save(this.output)
        }

        mapOf(
            Items.BRICK to Items.BRICKS,
            Items.NETHER_BRICK to Items.NETHER_BRICKS,
            Items.PRISMARINE_SHARD to Items.PRISMARINE,
            Items.SNOWBALL to Items.SNOW_BLOCK,
        ).forEach { (output: Item, input: Item) ->
            HCRecipeBuilders.crushing {
                ingredient = itemCreator.create(input)
                result { +(output to 4) }
                recipeId suffix "_from_block"
            }.save(this.output)
        }

        // Prismarine Bricks -> Prismarine Shard
        HCRecipeBuilders.crushing {
            ingredient = itemCreator.create(Items.PRISMARINE_BRICKS)
            result { +(Items.PRISMARINE_SHARD to 9) }
            recipeId suffix "_from_bricks"
        }.save(output)
        // Beetroot -> Sugar + Molasses
        HCRecipeBuilders.crushing {
            ingredient = itemCreator.tag(Tags.Items.CROPS_BEETROOT)
            result { +(Items.SUGAR to 2) }
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_beetroot"
        }.save(output)
        // Sugar Cane -> Sugar + Molasses
        HCRecipeBuilders.crushing {
            ingredient = itemCreator.tag(Tags.Items.CROPS_SUGAR_CANE)
            result { +(Items.SUGAR to 4) }
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_cane"
        }.save(output)
        // Ice -> Snowball
        HCRecipeBuilders.crushing {
            ingredient = itemCreator.create(Items.ICE)
            result { +(Items.SNOWBALL to 4) }
            recipeId suffix "_from_ice"
        }.save(output)
        // Wheat -> Flour
        HCRecipeBuilders.crushing {
            ingredient = itemCreator.tag(Tags.Items.CROPS_WHEAT)
            // results += resultCreator.create(HCItems.WHEAT_FLOUR)
        }

        crushStones()
        crushWoods()
    }

    private fun crushStones() {
        // Stone -> Cobblestone
        HCRecipeBuilders.crushing {
            ingredient = itemCreator.create(Items.STONE)
            result { +Items.COBBLESTONE }
            recipeId suffix "_from_stone"
        }.save(output)
        // Cobblestone -> Gravel
        HCRecipeBuilders.crushing {
            ingredient = itemCreator.tags(listOf(Tags.Items.COBBLESTONES_NORMAL, Tags.Items.COBBLESTONES_MOSSY))
            result { +Items.GRAVEL }
            recipeId suffix "_from_cobblestone"
        }.save(output)
        // Gravel -> Sand
        HCRecipeBuilders.crushing {
            ingredient = itemCreator.tag(Tags.Items.GRAVELS)
            result { +Items.SAND }
            recipeId suffix "_from_gravel"
        }.save(output)
        // Sandstone -> Sand + Saltpeter
        HCRecipeBuilders.crushing {
            ingredient = itemCreator.tag(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            result { +(Items.SAND to 2) }
            result {
                +HTItemResult.MaterialPart(CommonPartKeys.DUST, CommonMaterialKeys.SALTPETER)
                chance = fraction(1, 4)
            }
            recipeId suffix "_from_sandstone"
        }.save(output)

        HCRecipeBuilders.crushing {
            ingredient = itemCreator.tag(Tags.Items.SANDSTONE_RED_BLOCKS)
            result { +(Items.RED_SAND to 2) }
            result {
                +HTItemResult.MaterialPart(CommonPartKeys.DUST, CommonMaterialKeys.SALTPETER)
                chance = fraction(1, 4)
            }
            recipeId suffix "_from_sandstone"
        }.save(output)
    }

    private fun crushWoods() {
        // Wood Dust
        fun wood(tagKey: TagKey<Item>, input: Int, output: Int) {
            HCRecipeBuilders.crushing {
                ingredient = itemCreator.tag(tagKey, input)
                result { +HTItemResult.MaterialPart(CommonPartKeys.DUST, VanillaMaterialKeys.WOOD, output) }
                recipeId replace id(HTConstants.MATERIAL, "wood", "from_${tagKey.location().path.replace("/", "_")}")
            }.save(this.output)
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

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCCrushingRecipeProvider) {
        override fun getName(): String = "Crushing Recipes"
    }
}
