package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.common.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
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
            HTSingleItemRecipeBuilder
                .crushing(output)
                .addIngredient(input)
                .setExp(0.1f)
                .save(this.output)
        }

        mapOf(
            HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, HCMaterial.Dusts.WOOD) to ItemTags.LOGS,
            Items.SAND to Tags.Items.SANDSTONE_UNCOLORED_BLOCKS,
            Items.RED_SAND to Tags.Items.SANDSTONE_RED_BLOCKS,
        ).forEach { (output: ItemLike, input: TagKey<Item>) ->
            HTSingleItemRecipeBuilder
                .crushing(output, 4)
                .addIngredient(input)
                .setExp(0.4f)
                .saveSuffixed(this.output, "_from_block")
        }

        HTSingleItemRecipeBuilder
            .crushing(Items.BRICK, 4)
            .addIngredient(Items.BRICKS)
            .setExp(0.1f)
            .saveSuffixed(output, "_from_bricks")

        HTSingleItemRecipeBuilder
            .crushing(Items.PRISMARINE_SHARD, 4)
            .addIngredient(Items.PRISMARINE)
            .setExp(0.4f)
            .save(output)

        HTSingleItemRecipeBuilder
            .crushing(Items.PRISMARINE_SHARD, 9)
            .addIngredient(Items.PRISMARINE_BRICKS)
            .setExp(0.9f)
            .saveSuffixed(output, "_from_bricks")

        HTSingleItemRecipeBuilder
            .crushing(Items.PRISMARINE_CRYSTALS)
            .addIngredient(Items.PRISMARINE_SHARD)
            .setExp(0.1f)
            .saveSuffixed(output, "_from_shard")

        HTSingleItemRecipeBuilder
            .crushing(Items.AMETHYST_SHARD, 4)
            .addIngredient(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Gems.AMETHYST)
            .setExp(0.4f)
            .save(output)

        HTSingleItemRecipeBuilder
            .crushing(Items.GRAVEL)
            .addIngredient(Tags.Items.COBBLESTONES)
            .setExp(0.1f)
            .save(output)

        HTSingleItemRecipeBuilder
            .crushing(Items.SAND)
            .addIngredient(Tags.Items.GRAVELS)
            .setExp(0.1f)
            .save(output)

        HTSingleItemRecipeBuilder
            .crushing(Items.SNOWBALL, 4)
            .addIngredient(Items.SNOW_BLOCK)
            .setExp(0.4f)
            .save(output)
    }
}
