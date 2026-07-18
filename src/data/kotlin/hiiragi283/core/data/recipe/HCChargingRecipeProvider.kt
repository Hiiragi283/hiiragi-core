package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.HCChargingRecipeBuilder
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class HCChargingRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipes() {
        // Ender Pearl -> Ender Eye
        HCChargingRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER) }
            result {
                +Items.ENDER_EYE
                chance = fraction(1, 2)
            }
        }.save(exporter)
        // Golden Apple
        HCChargingRecipeBuilder.create {
            ingredient { +Items.GOLDEN_APPLE }
            result {
                +Items.ENCHANTED_GOLDEN_APPLE
                chance = fraction(1, 8)
            }
        }.save(exporter)
        // Quartz -> Prismarine
        HCChargingRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ) }
            result {
                +Items.PRISMARINE_SHARD
                chance = fraction(3, 4)
            }
        }.save(exporter)
        // Redstone Dust -> Glowstone Dust
        HCChargingRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE) }
            result {
                +HTItemResult.MaterialPart(CommonParts.DUST, VanillaMaterialKeys.GLOWSTONE)
                chance = fraction(3, 4)
            }
        }.save(exporter)
        // Honey Bottle -> Exp Bottle
        HCChargingRecipeBuilder.create {
            ingredient { +Tags.Items.DRINKS_HONEY }
            result {
                +Items.EXPERIENCE_BOTTLE
                chance = fraction(1, 2)
            }
        }.save(exporter)

        // End Crystal -> Eldritch Pearl
        HCChargingRecipeBuilder.create {
            ingredient { +Items.END_CRYSTAL }
            result {
                +HTItemResult.MaterialPart(CommonParts.PEARL, HCMaterialKeys.ELDRITCH)
                chance = fraction(1, 4)
            }
        }.save(exporter)
        // Heart of the Sea
        HCChargingRecipeBuilder.create {
            ingredient { +HCItems.ELDER_HEART }
            result { +Items.HEART_OF_THE_SEA }
        }.save(exporter)
        // End Crystal -> Eldritch Pearl
        HCChargingRecipeBuilder.create {
            ingredient { +HCItems.WITHER_STAR }
            result { +Items.NETHER_STAR }
        }.save(exporter)
    }

    override fun getName(): String = "Charging Recipes"
}
