package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HCAnvilCrushingRecipeBuilder
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HCCrushingRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        mapOf(
            HCItems.LUMINOUS_PASTE to Items.GLOW_INK_SAC,
            HCItems.MAGMA_SHARD to Items.MAGMA_BLOCK,
        ).forEach { (output: ItemLike, input: ItemLike) ->
            HCAnvilCrushingRecipeBuilder.create(this.output) {
                ingredient = inputCreator.create(input)
                result = resultCreator.create(output)
            }
        }

        mapOf(
            HiiragiCoreAccess.INSTANCE.materialContents.getItemOrThrow(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD) to ItemTags.LOGS,
            Items.SAND to Tags.Items.SANDSTONE_UNCOLORED_BLOCKS,
            Items.RED_SAND to Tags.Items.SANDSTONE_RED_BLOCKS,
        ).forEach { (output: ItemLike, input: TagKey<Item>) ->
            HCAnvilCrushingRecipeBuilder.create(this.output) {
                ingredient = inputCreator.create(input)
                result = resultCreator.create(output, 4)
                recipeId suffix "_from_block"
            }
        }

        HCAnvilCrushingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.BRICKS)
            result = resultCreator.create(Items.BRICK, 4)
            recipeId suffix "_from_bricks"
        }

        HCAnvilCrushingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.PRISMARINE)
            result = resultCreator.create(Items.PRISMARINE_SHARD, 4)
        }

        HCAnvilCrushingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.PRISMARINE_BRICKS)
            result = resultCreator.create(Items.PRISMARINE_SHARD, 9)
            recipeId suffix "_from_bricks"
        }

        HCAnvilCrushingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.PRISMARINE_SHARD)
            result = resultCreator.create(Items.PRISMARINE_CRYSTALS)
            recipeId suffix "_from_shard"
        }

        HCAnvilCrushingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.COBBLESTONES)
            result = resultCreator.create(Items.GRAVEL)
        }

        HCAnvilCrushingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.GRAVELS)
            result = resultCreator.create(Items.SAND)
        }

        HCAnvilCrushingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.SNOW_BLOCK)
            result = resultCreator.create(Items.SNOWBALL, 4)
        }

        HCAnvilCrushingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.CALCITE)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.CALCITE)
        }
    }
}
