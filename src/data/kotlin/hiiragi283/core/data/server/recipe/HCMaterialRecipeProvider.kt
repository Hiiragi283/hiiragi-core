package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialPrefixes
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object HCMaterialRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        ingotToGear()
        ingotToNugget()
    }

    @JvmStatic
    private fun ingotToGear() {
        for ((key: HTMaterialKey, gear: HTSimpleDeferredItem) in HCItems.MATERIALS.row(CommonMaterialPrefixes.GEAR)) {
            // Shaped
            HTShapedRecipeBuilder
                .create(gear)
                .hollow4()
                .define('A', CommonMaterialPrefixes.INGOT, key)
                .define('B', CommonMaterialPrefixes.NUGGET, HCMaterial.Metals.IRON)
                .save(output)
        }
    }

    @JvmStatic
    fun ingotToNugget() {
        for ((key: HTMaterialKey, nugget: HTSimpleDeferredItem) in HCItems.MATERIALS.row(CommonMaterialPrefixes.NUGGET)) {
            // Shapeless
            HTShapelessRecipeBuilder
                .create(nugget, 9)
                .addIngredient(CommonMaterialPrefixes.INGOT, key)
                .saveSuffixed(output, "_from_ingot")
            // Shaped
            val ingot: ItemLike = when {
                HCMaterial.Metals.COPPER.isOf(key) -> Items.COPPER_INGOT
                HCMaterial.Alloys.NETHERITE.isOf(key) -> Items.NETHERITE_INGOT
                else -> HCItems.MATERIALS[CommonMaterialPrefixes.INGOT, key]
            } ?: continue
            HTShapedRecipeBuilder
                .create(ingot)
                .hollow8()
                .define('A', CommonMaterialPrefixes.NUGGET, key)
                .define('B', nugget)
                .saveSuffixed(output, "_from_nugget")
        }
    }
}
