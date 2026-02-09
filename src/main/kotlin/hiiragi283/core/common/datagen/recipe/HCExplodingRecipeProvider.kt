package hiiragi283.core.common.datagen.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
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

object HCExplodingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipes() {
        // Cobblestone -> Cobbled Deepslate
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.STONES, Tags.Items.COBBLESTONES_NORMAL), amount = 2)
            result = resultCreator.create(Items.COBBLED_DEEPSLATE)
            minPower = fraction(3f)
        }
        // Ancient Debris -> Netherite Scrap
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.ORES_NETHERITE_SCRAP)
            result = resultCreator.material(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2)
            minPower = fraction(6f)
        }
        // Gunpowder -> Blaze Powder
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.GUNPOWDERS, 3)
            result = resultCreator.create(Items.BLAZE_POWDER)
        }
        // Glass -> Quartz
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.GLASS_BLOCKS, 4)
            result = resultCreator.material(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ)
        }
        // Quartz Block -> Ghast Tear
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.QUARTZ, 4)
            result = resultCreator.create(Items.GHAST_TEAR)
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
                ingredient = inputCreator.create(fuels.flatMap(::baseOrDust), count)
                result = resultCreator.material(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
                recipeId suffix "_from_${fuels.joinToString(separator = "_or_", transform = HTMaterialKey::path)}"
            }
        }

        // Echo Shard
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.SCULK, 8)
            result = resultCreator.material(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO)
            minPower = fraction(6f)
        }

        // Crimson Crystal
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.CRIMSON_STEMS, 12)
            result = resultCreator.material(CommonTagPrefixes.GEM, HCMaterialKeys.CRIMSON_CRYSTAL)
        }
        // Warped Crystal
        HCExplodingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.WARPED_STEMS, 12)
            result = resultCreator.material(CommonTagPrefixes.GEM, HCMaterialKeys.WARPED_CRYSTAL)
        }
    }
}
