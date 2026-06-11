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
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

class HCCrushingRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipes() {
        mapOf(
            Items.NETHER_WART to Items.NETHER_WART_BLOCK,
            HCBlocks.WARPED_WART to Items.WARPED_WART_BLOCK,
        ).forEach { (output: ItemLike, input: Item) ->
            HCRecipeBuilders.crushing {
                ingredient { items { +input } }
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
                ingredient { items { +input } }
                result {
                    +output
                    count = 4
                }
                recipeId suffix "_from_block"
            }.save(exporter)
        }

        // Prismarine Bricks -> Prismarine Shard
        HCRecipeBuilders.crushing {
            ingredient { items { +Items.PRISMARINE_BRICKS } }
            result {
                +Items.PRISMARINE_SHARD
                count = 9
            }
            recipeId suffix "_from_bricks"
        }.save(exporter)
        // Beetroot -> Sugar + Molasses
        HCRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.CROPS_BEETROOT) }
            result {
                +Items.SUGAR
                count = 2
            }
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_beetroot"
        }.save(exporter)
        // Sugar Cane -> Sugar + Molasses
        HCRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.CROPS_SUGAR_CANE) }
            result {
                +Items.SUGAR
                count = 4
            }
            // extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_cane"
        }.save(exporter)
        // Ice -> Snowball
        HCRecipeBuilders.crushing {
            ingredient { items { +Items.ICE } }
            result {
                +Items.SNOWBALL
                count = 4
            }
            recipeId suffix "_from_ice"
        }.save(exporter)
        // Wheat -> Flour
        HCRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.CROPS_WHEAT) }
            // results += resultCreator.create(HCItems.WHEAT_FLOUR)
        }

        crushStones()
        crushWoods()
    }

    private fun crushStones() {
        // Stone -> Cobblestone
        HCRecipeBuilders.crushing {
            ingredient { items { +Items.STONE } }
            result { +Items.COBBLESTONE }
            recipeId suffix "_from_stone"
        }.save(exporter)
        // Cobblestone -> Gravel
        HCRecipeBuilders.crushing {
            ingredient { +listOf(holderSet(Tags.Items.COBBLESTONES_NORMAL), holderSet(Tags.Items.COBBLESTONES_MOSSY)) }
            result { +Items.GRAVEL }
            recipeId suffix "_from_cobblestone"
        }.save(exporter)
        // Gravel -> Sand
        HCRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.GRAVELS) }
            result { +Items.SAND }
            recipeId suffix "_from_gravel"
        }.save(exporter)
        // Sandstone -> Sand + Saltpeter
        HCRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS) }
            result {
                +Items.SAND
                count = 2
            }
            result {
                +HTItemResult.MaterialPart(CommonPartKeys.DUST, CommonMaterialKeys.SALTPETER)
                chance = fraction(1, 4)
            }
            recipeId suffix "_from_sandstone"
        }.save(exporter)

        HCRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.SANDSTONE_RED_BLOCKS) }
            result {
                +Items.RED_SAND
                count = 2
            }
            result {
                +HTItemResult.MaterialPart(CommonPartKeys.DUST, CommonMaterialKeys.SALTPETER)
                chance = fraction(1, 4)
            }
            recipeId suffix "_from_sandstone"
        }.save(exporter)
    }

    private fun crushWoods() {
        // Wood Dust
        fun wood(tagKey: TagKey<Item>, input: Int, output: Int) {
            HCRecipeBuilders.crushing {
                ingredient {
                    +holderSet(tagKey)
                    count = input
                }
                result { +HTItemResult.MaterialPart(CommonPartKeys.DUST, VanillaMaterialKeys.WOOD, output) }
                recipeId replace id(HTConstants.MATERIAL, "wood", "from_${tagKey.location().path.replace("/", "_")}")
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
