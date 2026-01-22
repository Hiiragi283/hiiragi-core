package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Items

object HCChargingRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Ender Pearl -> Ender Eye
        HTSingleItemRecipeBuilder
            .charging(
                inputCreator.create(CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER),
                itemResult.create(Items.ENDER_EYE),
            ).save(output)
        // Golden Apple
        HTSingleItemRecipeBuilder
            .charging(
                inputCreator.create(Items.GOLDEN_APPLE),
                itemResult.create(Items.ENCHANTED_GOLDEN_APPLE),
            ).save(output)
        // Quartz -> Prismarine
        HTSingleItemRecipeBuilder
            .charging(
                inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ),
                itemResult.create(Items.PRISMARINE_SHARD),
            ).save(output)
        // Redstone Dust -> Glowstone Dust
        HTSingleItemRecipeBuilder
            .charging(
                inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
                itemResult.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE),
            ).save(output)

        // End Crystal -> Eldritch Pearl
        HTSingleItemRecipeBuilder
            .charging(
                inputCreator.create(Items.END_CRYSTAL),
                itemResult.create(CommonTagPrefixes.PEARL, HCMaterialKeys.ELDRITCH),
            ).save(output)
        // Heart of the Sea
        HTSingleItemRecipeBuilder
            .charging(
                inputCreator.create(HCItems.ELDER_HEART),
                itemResult.create(Items.HEART_OF_THE_SEA),
            ).save(output)
        // Nether Star
        HTSingleItemRecipeBuilder
            .charging(
                inputCreator.create(HCItems.WITHER_STAR),
                itemResult.create(Items.NETHER_STAR),
            ).save(output)
    }
}
