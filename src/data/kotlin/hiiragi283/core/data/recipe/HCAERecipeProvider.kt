package hiiragi283.core.data.recipe

import appeng.recipes.transform.TransformCircumstance
import appeng.recipes.transform.TransformRecipeBuilder
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Item

data object HCAERecipeProvider : HTSubRecipeProvider.Integration(HiiragiCoreAPI.MOD_ID, "ae2") {
    override fun buildRecipeInternal() {
        transform()
    }

    @JvmStatic
    private fun transform() {
        // Steel Ingot
        TransformRecipeBuilder.transform(
            output,
            id("transform", "steel_ingot_with_charcoal"),
            getOrThrow(CommonParts.INGOT, CommonMaterialKeys.STEEL),
            1,
            TransformCircumstance.EXPLOSION,
            itemCreator.create(baseOrDust(VanillaMaterialKeys.IRON)),
            itemCreator.create(baseOrDust(VanillaMaterialKeys.CHARCOAL)),
        )
        TransformRecipeBuilder.transform(
            output,
            id("transform", "steel_ingot_with_coal"),
            getOrThrow(CommonParts.INGOT, CommonMaterialKeys.STEEL),
            1,
            TransformCircumstance.EXPLOSION,
            itemCreator.create(baseOrDust(VanillaMaterialKeys.IRON)),
            itemCreator.create(baseOrDust(VanillaMaterialKeys.COAL)),
        )
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
        TransformRecipeBuilder.transform(
            output,
            id("transform", "azure_shard"),
            getOrThrow(CommonParts.GEM, HCMaterialKeys.AZURE),
            1,
            TransformCircumstance.EXPLOSION,
            itemCreator.create(baseOrDust(VanillaMaterialKeys.AMETHYST)),
            itemCreator.create(baseOrDust(VanillaMaterialKeys.LAPIS)),
        )
        // Azure Steel
        TransformRecipeBuilder.transform(
            output,
            id("transform", "azure_steel_ingot"),
            getOrThrow(CommonParts.INGOT, HCMaterialKeys.AZURE_STEEL),
            1,
            TransformCircumstance.EXPLOSION,
            itemCreator.create(baseOrDust(VanillaMaterialKeys.IRON)),
            itemCreator.create(baseOrDust(HCMaterialKeys.AZURE)),
        )
    }

    @JvmStatic
    private fun getOrThrow(part: HTPartLike, material: HTMaterialLike): Item = HiiragiCoreAccess.INSTANCE
        .registeredContents
        .items
        .getOrThrow(part, material)
        .get()
}
