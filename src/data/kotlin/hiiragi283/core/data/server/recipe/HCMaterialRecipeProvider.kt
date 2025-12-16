package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object HCMaterialRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        baseToDust()
        baseToBlock()

        ingotToGear()
        ingotToNugget()
    }

    @JvmStatic
    private fun baseToDust() {
        for (material: HCMaterial in HCMaterial.entries) {
            val basePrefix: HTMaterialPrefix = material.basePrefix
            if (basePrefix == HCMaterialPrefixes.DUST) continue
            val dust: HTSimpleDeferredItem = HCItems.MATERIALS[HCMaterialPrefixes.DUST, material] ?: continue
            // Shaped
            HTShapedRecipeBuilder
                .create(dust, 2)
                .pattern(
                    "AB",
                    "CB",
                ).define('A', Items.FLINT)
                .define('B', basePrefix, material)
                .define('C', Items.BOWL)
                .saveSuffixed(output, "_with_manual")
        }
    }

    @JvmStatic
    private fun baseToBlock() {
        for (material: HCMaterial in HCMaterial.entries) {
            val basePrefix: HTMaterialPrefix = material.basePrefix
            val block: ItemLike = HCBlocks.MATERIAL[HCMaterialPrefixes.STORAGE_BLOCK, material] ?: continue
            val base: ItemLike = when (material) {
                HCMaterial.Fuels.CHARCOAL -> Items.CHARCOAL
                HCMaterial.Gems.ECHO -> Items.ECHO_SHARD
                HCMaterial.Pearls.ENDER -> Items.ENDER_PEARL
                else -> HCItems.MATERIALS[basePrefix, material]
            } ?: continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(base, 9)
                .addIngredient(HCMaterialPrefixes.STORAGE_BLOCK, material)
                .saveSuffixed(output, "_from_block")
            // Shaped
            HTShapedRecipeBuilder
                .create(block)
                .hollow8()
                .define('A', basePrefix, material)
                .define('B', base)
                .saveSuffixed(output, "_from_${basePrefix.name}")
        }
    }

    @JvmStatic
    private fun ingotToGear() {
        for ((key: HTMaterialKey, gear: HTSimpleDeferredItem) in HCItems.MATERIALS.row(HCMaterialPrefixes.GEAR)) {
            // Shaped
            HTShapedRecipeBuilder
                .create(gear)
                .hollow4()
                .define('A', HCMaterialPrefixes.INGOT, key)
                .define('B', HCMaterialPrefixes.NUGGET, HCMaterial.Metals.IRON)
                .save(output)
        }
    }

    @JvmStatic
    fun ingotToNugget() {
        for ((key: HTMaterialKey, nugget: HTSimpleDeferredItem) in HCItems.MATERIALS.row(HCMaterialPrefixes.NUGGET)) {
            // Shapeless
            HTShapelessRecipeBuilder
                .create(nugget, 9)
                .addIngredient(HCMaterialPrefixes.INGOT, key)
                .saveSuffixed(output, "_from_ingot")
            // Shaped
            val ingot: ItemLike = when {
                HCMaterial.Metals.COPPER.isOf(key) -> Items.COPPER_INGOT
                HCMaterial.Alloys.NETHERITE.isOf(key) -> Items.NETHERITE_INGOT
                else -> HCItems.MATERIALS[HCMaterialPrefixes.INGOT, key]
            } ?: continue
            HTShapedRecipeBuilder
                .create(ingot)
                .hollow8()
                .define('A', HCMaterialPrefixes.NUGGET, key)
                .define('B', nugget)
                .saveSuffixed(output, "_from_nugget")
        }
    }
}
