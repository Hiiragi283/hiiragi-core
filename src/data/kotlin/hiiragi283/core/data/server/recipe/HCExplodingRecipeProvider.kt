package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HCExplodingRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HCExplodingRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Cobblestone -> Cobbled Deepslate
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.STONES, Tags.Items.COBBLESTONES_NORMAL), amount = 2)
            result = itemResult.create(Items.COBBLED_DEEPSLATE)
            minPower = fraction(3f)
        }
        // Ancient Debris -> Netherite Scrap
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.ORES_NETHERITE_SCRAP)
            result = itemResult.create(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2)
            minPower = fraction(6f)
        }
        // Gunpowder -> Blaze Powder
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.GUNPOWDERS, 3)
            result = itemResult.create(Items.BLAZE_POWDER)
        }
        // Glass -> Quartz
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.GLASS_BLOCKS, 4)
            result = itemResult.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ)
        }
        // Quartz Block -> Ghast Tear
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.QUARTZ, 4)
            result = itemResult.create(Items.GHAST_TEAR)
            minPower = fraction(3f)
        }

        gems()
    }

    @JvmStatic
    private fun gems() {
        // Diamond
        mapOf(
            listOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL) to 64,
            listOf(CommonMaterialKeys.COAL_COKE) to 32,
        ).forEach { (fuels: List<HTMaterialKey>, count: Int) ->
            HCExplodingRecipeBuilder.create(output) {
                ingredient = inputCreator.create(
                    listOf(CommonTagPrefixes.DUST, CommonTagPrefixes.FUEL),
                    fuels,
                    count,
                )
                result = itemResult.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
                recipeId suffix "_from_${fuels.joinToString(separator = "_or_", transform = HTMaterialKey::path)}"
            }
        }

        // Echo Shard
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.SCULK, 8)
            result = itemResult.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO)
            minPower = fraction(6f)
        }

        // Crimson Crystal
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.CRIMSON_STEMS, 12)
            result = itemResult.create(CommonTagPrefixes.GEM, HCMaterialKeys.CRIMSON_CRYSTAL)
        }
        // Warped Crystal
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.WARPED_STEMS, 12)
            result = itemResult.create(CommonTagPrefixes.GEM, HCMaterialKeys.WARPED_CRYSTAL)
        }
    }
}
