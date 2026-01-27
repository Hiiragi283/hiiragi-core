package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HCSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Items

object HCChargingRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Ender Pearl -> Ender Eye
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER)
            result = itemResult.create(Items.ENDER_EYE)
        }
        // Golden Apple
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(Items.GOLDEN_APPLE)
            result = itemResult.create(Items.ENCHANTED_GOLDEN_APPLE)
        }
        // Quartz -> Prismarine
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ)
            result = itemResult.create(Items.PRISMARINE_SHARD)
        }
        // Redstone Dust -> Glowstone Dust
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            result = itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE)
        }

        // End Crystal -> Eldritch Pearl
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(Items.END_CRYSTAL)
            result = itemResult.create(CommonTagPrefixes.PEARL, HCMaterialKeys.ELDRITCH)
        }
        // Heart of the Sea
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(HCItems.ELDER_HEART)
            result = itemResult.create(Items.HEART_OF_THE_SEA)
        }
        // Nether Star
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(HCItems.WITHER_STAR)
            result = itemResult.create(Items.NETHER_STAR)
        }
    }
}
