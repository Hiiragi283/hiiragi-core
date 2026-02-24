package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HTItemToItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Items

object HCChargingRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Ender Pearl -> Ender Eye
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER)
            result = resultCreator.create(Items.ENDER_EYE)
        }
        // Golden Apple
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(Items.GOLDEN_APPLE)
            result = resultCreator.create(Items.ENCHANTED_GOLDEN_APPLE)
        }
        // Quartz -> Prismarine
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ)
            result = resultCreator.create(Items.PRISMARINE_SHARD)
        }
        // Redstone Dust -> Glowstone Dust
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE)
        }

        // End Crystal -> Eldritch Pearl
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(Items.END_CRYSTAL)
            result = resultCreator.material(CommonTagPrefixes.PEARL, HCMaterialKeys.ELDRITCH)
        }
        // Heart of the Sea
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(HCItems.ELDER_HEART)
            result = resultCreator.create(Items.HEART_OF_THE_SEA)
        }
        // Nether Star
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(HCItems.WITHER_STAR)
            result = resultCreator.create(Items.NETHER_STAR)
        }
    }
}
