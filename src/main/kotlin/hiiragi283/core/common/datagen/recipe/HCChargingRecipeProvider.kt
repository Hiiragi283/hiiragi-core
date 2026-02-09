package hiiragi283.core.common.datagen.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HCSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Items

object HCChargingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipes() {
        // Ender Pearl -> Ender Eye
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER)
            result = resultCreator.create(Items.ENDER_EYE)
        }
        // Golden Apple
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(Items.GOLDEN_APPLE)
            result = resultCreator.create(Items.ENCHANTED_GOLDEN_APPLE)
        }
        // Quartz -> Prismarine
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ)
            result = resultCreator.create(Items.PRISMARINE_SHARD)
        }
        // Redstone Dust -> Glowstone Dust
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE)
        }

        // End Crystal -> Eldritch Pearl
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(Items.END_CRYSTAL)
            result = resultCreator.material(CommonTagPrefixes.PEARL, HCMaterialKeys.ELDRITCH)
        }
        // Heart of the Sea
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(HCItems.ELDER_HEART)
            result = resultCreator.create(Items.HEART_OF_THE_SEA)
        }
        // Nether Star
        HCSingleItemRecipeBuilder.charging(output) {
            ingredient = inputCreator.create(HCItems.WITHER_STAR)
            result = resultCreator.create(Items.NETHER_STAR)
        }
    }
}
