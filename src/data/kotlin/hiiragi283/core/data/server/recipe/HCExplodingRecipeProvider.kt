package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.tag.CommonTagPrefixes
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
                inputCreator.create(listOf(Tags.Items.STONES, Tags.Items.COBBLESTONES_NORMAL), amount = 2),
                itemResult.create(Items.COBBLED_DEEPSLATE),
                3f,
            ).save(output)
        // Ancient Debris -> Netherite Scrap
        HTSingleItemRecipeBuilder
            .exploding(
                inputCreator.create(Tags.Items.ORES_NETHERITE_SCRAP),
                itemResult.create(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2),
                6f,
            ).save(output)
        // Gunpowder -> Blaze Powder
        HTSingleItemRecipeBuilder
            .exploding(
                inputCreator.create(Tags.Items.GUNPOWDERS, 3),
                itemResult.create(Items.BLAZE_POWDER),
            ).save(output)
        // Glass -> Quartz
        HTSingleItemRecipeBuilder
            .exploding(
                inputCreator.create(Tags.Items.GLASS_BLOCKS, 4),
                itemResult.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ),
            ).save(output)
        // Quartz Block -> Ghast Tear
        HTSingleItemRecipeBuilder
            .exploding(
                inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.QUARTZ, 4),
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
                    inputCreator.create(
                        listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.FUEL),
                        fuels,
                        count,
                    ),
                    itemResult.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND),
                ).saveSuffixed(output, "_from_${fuels.joinToString(separator = "_or_", transform = HTMaterialKey::path)}")
        }

        // Echo Shard
        HTSingleItemRecipeBuilder
            .exploding(
                inputCreator.create(Items.SCULK, 8),
                itemResult.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO),
                6f,
            ).save(output)
        // Crimson Crystal
        HTSingleItemRecipeBuilder
            .exploding(
                inputCreator.create(ItemTags.CRIMSON_STEMS, 12),
                itemResult.create(CommonTagPrefixes.GEM, HCMaterialKeys.CRIMSON_CRYSTAL),
            ).save(output)
        // Warped Crystal
        HTSingleItemRecipeBuilder
            .exploding(
                inputCreator.create(ItemTags.WARPED_STEMS, 12),
                itemResult.create(CommonTagPrefixes.GEM, HCMaterialKeys.WARPED_CRYSTAL),
            ).save(output)
    }
}
