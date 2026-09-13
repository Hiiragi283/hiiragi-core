package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.data.recipe.HCRecipeBuilders
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

class HCCrushingRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipes() {
        mapOf(
            HCItems.LUMINOUS_PASTE to Items.GLOW_INK_SAC,
            HCItems.MAGMA_SHARD to Items.MAGMA_BLOCK,
        ).forEach { (output: ItemLike, input: Item) ->
            HCRecipeBuilders.crushing {
                ingredient { +input }
                result { +output }
            }.save(exporter)
        }

        mapOf(
            Items.NETHER_WART to Items.NETHER_WART_BLOCK,
            HCBlocks.WARPED_WART to Items.WARPED_WART_BLOCK,
        ).forEach { (output: ItemLike, input: Item) ->
            HCRecipeBuilders.crushing {
                ingredient { +input }
                result {
                    +output
                    count = 3
                }
            }.save(exporter)
        }

        mapOf(
            Items.BRICK to Items.BRICKS,
            Items.NETHER_BRICK to Items.NETHER_BRICKS,
            Items.PRISMARINE_SHARD to Items.PRISMARINE,
            Items.SNOWBALL to Items.SNOW_BLOCK,
        ).forEach { (output: Item, input: Item) ->
            HCRecipeBuilders.crushing {
                ingredient { +input }
                result {
                    +output
                    count = 4
                }
                recipeId suffix "_from_block"
            }.save(exporter)
        }
        // Prismarine Bricks -> Prismarine Shard
        HCRecipeBuilders.crushing {
            ingredient { +Items.PRISMARINE_BRICKS }
            result {
                +Items.PRISMARINE_SHARD
                count = 9
            }
            recipeId suffix "_from_bricks"
        }.save(exporter)
        // Beetroot -> Sugar + Molasses
        HCRecipeBuilders.crushing {
            ingredient { +Tags.Items.CROPS_BEETROOT }
            result {
                +Items.SUGAR
                count = 2
            }
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_beetroot"
        }.save(exporter)
        // Sugar Cane -> Sugar + Molasses
        HCRecipeBuilders.crushing {
            ingredient { +Tags.Items.CROPS_SUGAR_CANE }
            result {
                +Items.SUGAR
                count = 4
            }
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_cane"
        }.save(exporter)
        // Ice -> Snowball
        HCRecipeBuilders.crushing {
            ingredient { +Items.ICE }
            result {
                +Items.SNOWBALL
                count = 4
            }
            recipeId suffix "_from_ice"
        }.save(exporter)
        // Wheat -> Flour
        HCRecipeBuilders.crushing {
            ingredient { +Tags.Items.CROPS_WHEAT }
            result { +HCItems.WHEAT_FLOUR }
        }.save(exporter)

        crushStones()
        crushWoods()
    }

    private fun crushStones() {
        // Stone -> Cobblestone
        HCRecipeBuilders.crushing {
            ingredient { +Items.STONE }
            result { +Items.COBBLESTONE }
            recipeId suffix "_from_stone"
        }.save(exporter)
        // Cobblestone -> Gravel
        HCRecipeBuilders.crushing {
            ingredient { +listOf(Tags.Items.COBBLESTONES_NORMAL, Tags.Items.COBBLESTONES_MOSSY) }
            result { +Items.GRAVEL }
            recipeId suffix "_from_cobblestone"
        }.save(exporter)
        // Gravel -> Sand
        HCRecipeBuilders.crushing {
            ingredient { +Tags.Items.GRAVELS }
            result { +Items.SAND }
            recipeId suffix "_from_gravel"
        }.save(exporter)
        // Sandstone -> Sand + Saltpeter
        HCRecipeBuilders.crushing {
            ingredient { +Tags.Items.SANDSTONE_UNCOLORED_BLOCKS }
            result {
                +Items.SAND
                count = 2
            }
            result {
                +HTItemResult.MaterialPartEntry(CommonParts.DUST, CommonMaterialKeys.SALTPETER)
                chance = fraction(1, 4)
            }
            recipeId suffix "_from_sandstone"
        }.save(exporter)

        HCRecipeBuilders.crushing {
            ingredient { +Tags.Items.SANDSTONE_RED_BLOCKS }
            result {
                +Items.RED_SAND
                count = 2
            }
            result {
                +HTItemResult.MaterialPartEntry(CommonParts.DUST, CommonMaterialKeys.SALTPETER)
                chance = fraction(1, 4)
            }
            recipeId suffix "_from_sandstone"
        }.save(exporter)
    }

    private fun crushWoods() {
        fun wood(tagKey: TagKey<Item>, input: Int, output: Int) {
            HCRecipeBuilders.crushing {
                ingredient {
                    +tagKey
                    count = input
                }
                result {
                    +HTItemResult.MaterialPartEntry(CommonParts.DUST, VanillaMaterialKeys.WOOD)
                    count = output
                }
                recipeId suffix "_from_${tagKey.location().path}"
            }.save(exporter)
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

    override fun getName(): String = "Crushing Recipes"
}
