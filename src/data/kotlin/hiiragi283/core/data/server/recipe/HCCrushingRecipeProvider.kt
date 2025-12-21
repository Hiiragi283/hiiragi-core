package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
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
                .crushing(itemCreator.fromItem(input), resultHelper.item(output))
                .setExp(0.1f)
                .save(this.output)
        }

        mapOf(
            HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, HCMaterial.Wood) to ItemTags.LOGS,
            Items.SAND to Tags.Items.SANDSTONE_UNCOLORED_BLOCKS,
            Items.RED_SAND to Tags.Items.SANDSTONE_RED_BLOCKS,
        ).forEach { (output: ItemLike, input: TagKey<Item>) ->
            HTSingleItemRecipeBuilder
                .crushing(itemCreator.fromTagKey(input), resultHelper.item(output, 4))
                .setExp(0.4f)
                .saveSuffixed(this.output, "_from_block")
        }

        HTSingleItemRecipeBuilder
            .crushing(itemCreator.fromItem(Items.BRICKS), resultHelper.item(Items.BRICK, 4))
            .setExp(0.1f)
            .saveSuffixed(output, "_from_bricks")

        HTSingleItemRecipeBuilder
            .crushing(itemCreator.fromItem(Items.PRISMARINE), resultHelper.item(Items.PRISMARINE_SHARD, 4))
            .setExp(0.4f)
            .save(output)

        HTSingleItemRecipeBuilder
            .crushing(itemCreator.fromItem(Items.PRISMARINE_BRICKS), resultHelper.item(Items.PRISMARINE_SHARD, 9))
            .setExp(0.9f)
            .saveSuffixed(output, "_from_bricks")

        HTSingleItemRecipeBuilder
            .crushing(itemCreator.fromItem(Items.PRISMARINE_SHARD), resultHelper.item(Items.PRISMARINE_CRYSTALS))
            .setExp(0.1f)
            .saveSuffixed(output, "_from_shard")

        HTSingleItemRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Gems.AMETHYST),
                resultHelper.item(Items.AMETHYST_SHARD, 4),
            ).setExp(0.4f)
            .save(output)

        HTSingleItemRecipeBuilder
            .crushing(itemCreator.fromTagKey(Tags.Items.COBBLESTONES), resultHelper.item(Items.GRAVEL))
            .setExp(0.1f)
            .save(output)

        HTSingleItemRecipeBuilder
            .crushing(itemCreator.fromTagKey(Tags.Items.GRAVELS), resultHelper.item(Items.SAND))
            .setExp(0.1f)
            .save(output)

        HTSingleItemRecipeBuilder
            .crushing(itemCreator.fromItem(Items.SNOW_BLOCK), resultHelper.item(Items.SNOWBALL, 4))
            .setExp(0.4f)
            .save(output)
    }
}
