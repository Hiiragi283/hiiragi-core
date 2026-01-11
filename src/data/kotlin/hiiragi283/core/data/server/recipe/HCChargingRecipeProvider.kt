package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.data.recipe.builder.HTChargingRecipeBuilder
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Items

object HCChargingRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Ender Pearl -> Ender Eye
        HTChargingRecipeBuilder
            .create(
                itemCreator.fromTagKey(HCMaterialPrefixes.PEARL, VanillaMaterialKeys.ENDER),
                itemResult.create(Items.ENDER_EYE),
            ).save(output)
        // Golden Apple
        HTChargingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.GOLDEN_APPLE),
                itemResult.create(Items.ENCHANTED_GOLDEN_APPLE),
            ).save(output)
        // Quartz -> Prismarine
        HTChargingRecipeBuilder
            .create(
                itemCreator.fromTagKey(HCMaterialPrefixes.GEM, VanillaMaterialKeys.QUARTZ),
                itemResult.create(Items.PRISMARINE_SHARD),
            ).save(output)
        // Redstone Dust -> Glowstone Dust
        HTChargingRecipeBuilder
            .create(
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
                HCMaterialResultHelper.item(HCMaterialPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE),
            ).save(output)

        // End Crystal -> Eldritch Pearl
        HTChargingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.END_CRYSTAL),
                HCMaterialResultHelper.item(HCMaterialPrefixes.PEARL, HCMaterialKeys.ELDRITCH),
            ).save(output)
        // Heart of the Sea
        HTChargingRecipeBuilder
            .create(
                itemCreator.fromItem(HCItems.ELDER_HEART),
                itemResult.create(Items.HEART_OF_THE_SEA),
            ).setExp(10f)
            .save(output)
        // Nether Star
        HTChargingRecipeBuilder
            .create(
                itemCreator.fromItem(HCItems.WITHER_STAR),
                itemResult.create(Items.NETHER_STAR),
            ).setExp(10f)
            .save(output)
    }
}
