package hiiragi283.core.data.recipe

import appeng.recipes.transform.TransformCircumstance
import appeng.recipes.transform.TransformRecipeBuilder
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems

data object HCAERecipeProvider : HTSubRecipeProvider.Integration(HiiragiCoreAPI.MOD_ID, HCIConstants.AE2) {
    override fun buildRecipeInternal() {
        transform()
    }

    @JvmStatic
    private fun transform() {
        // Steel Ingot
        useItem(CommonParts.INGOT, CommonMaterialKeys.STEEL) {
            TransformRecipeBuilder.transform(
                output,
                id("transform", "steel_ingot_with_charcoal"),
                it,
                1,
                TransformCircumstance.EXPLOSION,
                itemCreator.create(baseOrDust(VanillaMaterialKeys.IRON)),
                itemCreator.create(baseOrDust(VanillaMaterialKeys.CHARCOAL)),
            )
            TransformRecipeBuilder.transform(
                output,
                id("transform", "steel_ingot_with_coal"),
                it,
                1,
                TransformCircumstance.EXPLOSION,
                itemCreator.create(baseOrDust(VanillaMaterialKeys.IRON)),
                itemCreator.create(baseOrDust(VanillaMaterialKeys.COAL)),
            )
        }
        // Cured Rubber
        TransformRecipeBuilder.transform(
            output,
            id("transform", "cured_rubber"),
            HCItems.CURED_RUBBER,
            2,
            TransformCircumstance.EXPLOSION,
            itemCreator.create(HCItems.RAW_RUBBER),
            itemCreator.create(baseOrDust(CommonMaterialKeys.SULFUR)),
        )

        // Azure Shard
        useItem(CommonParts.GEM, HCMaterialKeys.AZURE) {
            TransformRecipeBuilder.transform(
                output,
                id("transform", "azure_shard"),
                it,
                1,
                TransformCircumstance.EXPLOSION,
                itemCreator.create(baseOrDust(VanillaMaterialKeys.AMETHYST)),
                itemCreator.create(baseOrDust(VanillaMaterialKeys.LAPIS)),
            )
        }
        // Azure Steel
        useItem(CommonParts.INGOT, HCMaterialKeys.AZURE_STEEL) {
            TransformRecipeBuilder.transform(
                output,
                id("transform", "azure_steel_ingot"),
                it,
                1,
                TransformCircumstance.EXPLOSION,
                itemCreator.create(baseOrDust(VanillaMaterialKeys.IRON)),
                itemCreator.create(baseOrDust(HCMaterialKeys.AZURE)),
            )
        }
    }
}
