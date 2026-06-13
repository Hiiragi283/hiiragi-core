package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.math.fraction
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.tag.CommonTagPrefixes
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class HCExplodingRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipes() {
        // Cobblestone -> Cobbled Deepslate
        HCExplodingRecipeBuilder.create {
            ingredient { +listOf(holderSet(Tags.Items.STONES), holderSet(Tags.Items.COBBLESTONES_NORMAL)) }
            result {
                +Items.COBBLED_DEEPSLATE
                chance = fraction(1, 2)
            }
        }.save(exporter)
        // Gunpowder -> Blaze Powder
        HCExplodingRecipeBuilder.create {
            ingredient { +holderSet(Tags.Items.GUNPOWDERS) }
            result {
                +Items.BLAZE_POWDER
                chance = fraction(1, 6)
            }
        }.save(exporter)
        // Glass -> Quartz
        HCExplodingRecipeBuilder.create {
            ingredient { +holderSet(Tags.Items.GLASS_BLOCKS) }
            result {
                +HTItemResult.MaterialPart(CommonPartKeys.GEM, VanillaMaterialKeys.QUARTZ)
                chance = fraction(1, 4)
            }
        }.save(exporter)
        // Quartz Block -> Ghast Tear
        HCExplodingRecipeBuilder.create {
            ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ) }
            result {
                +Items.GHAST_TEAR
                chance = fraction(1, 4)
            }
        }.save(exporter)

        // Diamond
        HCExplodingRecipeBuilder.create {
            ingredient { +materialPart(HiiragiCoreTags.MaterialContents.COALS, CommonPartKeys.FUEL, CommonPartKeys.DUST) }
            result {
                +HTItemResult.MaterialPart(CommonPartKeys.GEM, VanillaMaterialKeys.DIAMOND)
                chance = fraction(1, 64)
            }
            recipeId suffix "_from_coal"
        }.save(exporter)
        // Echo Shard
        HCExplodingRecipeBuilder.create {
            ingredient { items { +Items.SCULK } }
            result {
                +HTItemResult.MaterialPart(CommonPartKeys.GEM, VanillaMaterialKeys.ECHO)
                chance = fraction(1, 8)
            }
        }.save(exporter)
    }

    override fun getName(): String = "Exploding Recipes"
}
