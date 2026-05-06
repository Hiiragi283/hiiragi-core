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
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.Tags

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
            TransformCircumstance.fluid(Tags.Fluids.LAVA),
            itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON),
            itemCreator.create(baseOrDust(VanillaMaterialKeys.CHARCOAL)),
        )
        TransformRecipeBuilder.transform(
            output,
            id("transform", "steel_ingot_with_coal"),
            getOrThrow(CommonParts.INGOT, CommonMaterialKeys.STEEL),
            1,
            TransformCircumstance.fluid(Tags.Fluids.LAVA),
            itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON),
            itemCreator.create(baseOrDust(VanillaMaterialKeys.COAL)),
        )
    }

    @JvmStatic
    private fun getOrThrow(part: HTPartLike, material: HTMaterialLike): Item = HiiragiCoreAccess.INSTANCE
        .registeredContents
        .items
        .getOrThrow(part, material)
        .get()
}
