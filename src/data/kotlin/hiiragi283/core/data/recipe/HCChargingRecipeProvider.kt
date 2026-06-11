package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
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

class HCChargingRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipes() {
        // Ender Pearl -> Ender Eye
        HCChargingRecipeBuilder.create {
            ingredient { +holderSet(Tags.Items.ENDER_PEARLS) }
            result {
                +Items.ENDER_EYE
                chance = fraction(1, 2)
            }
        }.save(exporter)
        // Golden Apple
        HCChargingRecipeBuilder.create {
            ingredient { items { +Items.GOLDEN_APPLE } }
            result {
                +Items.ENCHANTED_GOLDEN_APPLE
                chance = fraction(1, 8)
            }
        }.save(exporter)
        // Quartz -> Prismarine
        HCChargingRecipeBuilder.create {
            ingredient { +holderSet(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ) }
            result {
                +Items.PRISMARINE_SHARD
                chance = fraction(3, 4)
            }
        }.save(exporter)
        // Redstone Dust -> Glowstone Dust
        HCChargingRecipeBuilder.create {
            ingredient { +holderSet(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE) }
            result {
                +HTItemResult.MaterialPart(CommonPartKeys.DUST, VanillaMaterialKeys.GLOWSTONE)
                chance = fraction(3, 4)
            }
        }.save(exporter)
        // Honey Bottle -> Exp Bottle
        HCChargingRecipeBuilder.create {
            ingredient { +holderSet(Tags.Items.DRINKS_HONEY) }
            result {
                +Items.EXPERIENCE_BOTTLE
                chance = fraction(1, 2)
            }
        }.save(exporter)
    }

    override fun getName(): String = "Charging Recipes"
}
