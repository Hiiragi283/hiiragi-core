package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.HCExplodingRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class HCExplodingRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipes() {
        // Cobblestone -> Cobbled Deepslate
        HCExplodingRecipeBuilder.create {
            ingredient { +setOf(Tags.Items.STONES, Tags.Items.COBBLESTONES_NORMAL) }
            result {
                +Items.COBBLED_DEEPSLATE
                chance = fraction(1, 2)
            }
        }.save(exporter)
        // Ancient Debris -> Netherite Scrap
        HCExplodingRecipeBuilder.create {
            ingredient { +Tags.Items.ORES_NETHERITE_SCRAP }
            result { +HTItemResult.MaterialPart(CommonParts.SCRAP, VanillaMaterialKeys.NETHERITE, 2) }
        }.save(exporter)
        // Gunpowder -> Blaze Powder
        HCExplodingRecipeBuilder.create {
            ingredient { +Tags.Items.GUNPOWDERS }
            result {
                +Items.BLAZE_POWDER
                chance = fraction(1, 6)
            }
        }.save(exporter)
        // Glass -> Quartz// Glass -> Quartz
        HCExplodingRecipeBuilder.create {
            ingredient { +Tags.Items.GLASS_BLOCKS }
            result {
                +HTItemResult.MaterialPart(CommonParts.GEM, VanillaMaterialKeys.QUARTZ)
                chance = fraction(1, 4)
            }
        }.save(exporter)
        // Quartz Block -> Ghast Tear
        HCExplodingRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ) }
            result {
                +Items.GHAST_TEAR
                chance = fraction(1, 4)
            }
        }.save(exporter)

        // Diamond
        mapOf(
            listOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL) to 64,
            listOf(CommonMaterialKeys.COAL_COKE) to 32,
            listOf(CommonMaterialKeys.CARBON) to 16,
        ).forEach { (fuels: List<HTMaterialKey>, count: Int) ->
            HCExplodingRecipeBuilder.create {
                ingredient { +fuels.flatMap(::baseOrDust) }
                result {
                    +HTItemResult.MaterialPart(CommonParts.GEM, VanillaMaterialKeys.DIAMOND)
                    chance = fraction(1, count)
                }
                recipeId suffix "_from_${fuels.joinToString(separator = "_or_", transform = HTMaterialKey::name)}"
            }.save(exporter)
        }
        // Echo Shard
        HCExplodingRecipeBuilder.create {
            ingredient { +Items.SCULK }
            result {
                +HTItemResult.MaterialPart(CommonParts.GEM, VanillaMaterialKeys.ECHO)
                chance = fraction(1, 8)
            }
        }.save(exporter)
        // Crimson Crystal
        HCExplodingRecipeBuilder.create {
            ingredient { +ItemTags.CRIMSON_STEMS }
            result {
                +HTItemResult.MaterialPart(CommonParts.GEM, HCMaterialKeys.CRIMSON_CRYSTAL)
                chance = fraction(1, 8)
            }
        }.save(exporter)
        // Warped Crystal
        HCExplodingRecipeBuilder.create {
            ingredient { +ItemTags.WARPED_STEMS }
            result {
                +HTItemResult.MaterialPart(CommonParts.GEM, HCMaterialKeys.WARPED_CRYSTAL)
                chance = fraction(1, 8)
            }
        }.save(exporter)
    }

    override fun getName(): String = "Exploding Recipes"
}
