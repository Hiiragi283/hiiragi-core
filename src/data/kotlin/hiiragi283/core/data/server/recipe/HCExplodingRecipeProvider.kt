package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HCExplodingRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Cobblestone -> Cobbled Deepslate
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKeys(listOf(Tags.Items.STONES, Tags.Items.COBBLESTONES_NORMAL), amount = 2),
                resultHelper.item(Items.COBBLED_DEEPSLATE),
            ).setExp(0.1f)
            .save(output)
        // Ancient Debris -> Netherite Scrap
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(Tags.Items.ORES_NETHERITE_SCRAP),
                HCMaterialResultHelper.item(HCMaterialPrefixes.SCRAP, HCMaterial.Alloys.NETHERITE, 2),
            ).setExp(1f)
            .save(output)
        // Gunpowder -> Blaze Powder
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(Tags.Items.GUNPOWDERS, 3),
                resultHelper.item(Items.BLAZE_POWDER),
            ).setExp(0.5f)
            .save(output)
        // Glass -> Quartz
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(Tags.Items.GLASS_BLOCKS, 4),
                HCMaterialResultHelper.item(HCMaterialPrefixes.GEM, HCMaterial.Gems.QUARTZ),
            ).setExp(0.5f)
            .save(output)
        // Quartz Block -> Ghast Tear
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Gems.QUARTZ, 4),
                resultHelper.item(Items.GHAST_TEAR),
            ).setExp(0.5f)
            .save(output)

        gems()
    }

    @JvmStatic
    private fun gems() {
        // Diamond
        mapOf(
            listOf(HCMaterial.Fuels.COAL, HCMaterial.Fuels.CHARCOAL) to 64,
            listOf(HCMaterial.Fuels.COAL_COKE) to 32,
            listOf(HCMaterial.Fuels.CARBIDE) to 16,
        ).forEach { (fuels: List<HCMaterial.Fuels>, count: Int) ->
            HTSingleItemRecipeBuilder
                .exploding(
                    itemCreator.fromTagKeys(
                        listOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.FUEL),
                        fuels,
                        count,
                    ),
                    HCMaterialResultHelper.item(HCMaterialPrefixes.GEM, HCMaterial.Gems.DIAMOND),
                ).setExp(0.5f)
                .saveSuffixed(output, "_from_${fuels.joinToString(separator = "_or_", transform = HCMaterial::asMaterialName)}")
        }

        // Echo Shard
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromItem(Items.SCULK, 8),
                HCMaterialResultHelper.item(HCMaterialPrefixes.GEM, HCMaterial.Gems.ECHO),
            ).setExp(1f)
            .save(output)
        // Crimson Crystal
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(ItemTags.CRIMSON_STEMS, 12),
                HCMaterialResultHelper.item(HCMaterialPrefixes.GEM, HCMaterial.Gems.CRIMSON_CRYSTAL),
            ).setExp(1f)
            .save(output)
        // Warped Crystal
        HTSingleItemRecipeBuilder
            .exploding(
                itemCreator.fromTagKey(ItemTags.WARPED_STEMS, 12),
                HCMaterialResultHelper.item(HCMaterialPrefixes.GEM, HCMaterial.Gems.WARPED_CRYSTAL),
            ).setExp(1f)
            .save(output)
    }
}
