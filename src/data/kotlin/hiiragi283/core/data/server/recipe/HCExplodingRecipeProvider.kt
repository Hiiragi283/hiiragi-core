package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.HTMaterialResultHelper
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HCExplodingRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Cobblestone -> Cobbled Deepslate
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKeys(listOf(Tags.Items.STONES, Tags.Items.COBBLESTONES_NORMAL), amount = 2),
                itemResult.create(Items.COBBLED_DEEPSLATE),
                3f,
            ).save(output)
        // Ancient Debris -> Netherite Scrap
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(Tags.Items.ORES_NETHERITE_SCRAP),
                HTMaterialResultHelper.item(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2),
                6f,
            ).save(output)
        // Gunpowder -> Blaze Powder
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(Tags.Items.GUNPOWDERS, 3),
                itemResult.create(Items.BLAZE_POWDER),
            ).save(output)
        // Glass -> Quartz
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(Tags.Items.GLASS_BLOCKS, 4),
                HTMaterialResultHelper.item(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ),
            ).save(output)
        // Quartz Block -> Ghast Tear
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.QUARTZ, 4),
                itemResult.create(Items.GHAST_TEAR),
                3f,
            ).save(output)

        gems()
    }

    @JvmStatic
    private fun gems() {
        // Diamond
        mapOf(
            listOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL) to 64,
            listOf(CommonMaterialKeys.COAL_COKE) to 32,
        ).forEach { (fuels: List<HTMaterialKey>, count: Int) ->
            HTSingleItemRecipeBuilder
                .exploding(
                    itemCreator.fromTagKeys(
                        listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.FUEL),
                        fuels,
                        count,
                    ),
                    HTMaterialResultHelper.item(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND),
                ).saveSuffixed(output, "_from_${fuels.joinToString(separator = "_or_", transform = HTMaterialKey::getPath)}")
        }

        // Echo Shard
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromItem(Items.SCULK, 8),
                HTMaterialResultHelper.item(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO),
                6f,
            ).save(output)
        // Crimson Crystal
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(ItemTags.CRIMSON_STEMS, 12),
                HTMaterialResultHelper.item(CommonTagPrefixes.GEM, HCMaterialKeys.CRIMSON_CRYSTAL),
            ).save(output)
        // Warped Crystal
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(ItemTags.WARPED_STEMS, 12),
                HTMaterialResultHelper.item(CommonTagPrefixes.GEM, HCMaterialKeys.WARPED_CRYSTAL),
            ).save(output)
    }
}
