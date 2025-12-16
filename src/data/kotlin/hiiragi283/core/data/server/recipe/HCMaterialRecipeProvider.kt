package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.data.recipe.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.common.tag.HCCommonTags
import hiiragi283.core.common.tag.HCModTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object HCMaterialRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Amethyst + Lapis -> Azure Shard
        HTShapelessRecipeBuilder
            .create(HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, HCMaterial.Gems.AZURE), 4)
            .addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Gems.AMETHYST, 2)
            .addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Gems.LAPIS, 2)
            .saveSuffixed(output, "_from_mix")

        // Eldritch
        HTShapelessRecipeBuilder
            .create(HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, HCMaterial.Pearls.ELDRITCH), 2)
            .addIngredient(HCFluids.CRIMSON_BLOOD.bucketTag)
            .addIngredient(HCFluids.DEW_OF_THE_WARP.bucketTag)
            .addIngredient(HCModTags.Items.ELDRITCH_PEARL_BINDER)
            .saveSuffixed(output, "_from_mix")

        // Netherite Scrap + Azure Steel -> Deep Steel
        HTShapelessRecipeBuilder
            .create(HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, HCMaterial.Alloys.NETHERITE))
            .addIngredients(HCMaterialPrefixes.SCRAP, HCMaterial.Alloys.NETHERITE, 4)
            .addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Metals.GOLD, 4)
            .saveSuffixed(output, "_from_mix")
        // Azure Shard + Iron -> Azure Steel
        HTShapelessRecipeBuilder
            .create(HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, HCMaterial.Alloys.AZURE_STEEL))
            .addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Gems.AZURE, 2)
            .addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Metals.IRON)
            .saveSuffixed(output, "_from_mix")
        // Deep Scrap + Azure Steel -> Deep Steel
        HTShapelessRecipeBuilder
            .create(HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, HCMaterial.Alloys.DEEP_STEEL))
            .addIngredients(HCMaterialPrefixes.SCRAP, HCMaterial.Alloys.DEEP_STEEL, 4)
            .addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Alloys.AZURE_STEEL, 4)
            .saveSuffixed(output, "_from_mix")
        // Gold + Obsidian + Blackstone -> Night Metal
        HTShapelessRecipeBuilder
            .create(HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, HCMaterial.Alloys.NIGHT_METAL))
            .addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Metals.GOLD)
            .addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Dusts.OBSIDIAN)
            .addIngredients(Items.BLACKSTONE, count = 4)
            .saveSuffixed(output, "_from_mix")

        baseToDust()
        baseToBlock()

        dustOrRawToBase()

        ingotToGear()
        ingotToNugget()
    }

    @JvmStatic
    private fun baseToDust() {
        for (material: HCMaterial in HCMaterial.entries) {
            val dust: HTSimpleDeferredItem = HCItems.MATERIALS[HCMaterialPrefixes.DUST, material] ?: continue
            // Shaped
            HTShapelessRecipeBuilder
                .create(dust)
                .addIngredient(material.getBaseIngredient())
                .addIngredient(HCCommonTags.Items.TOOLS_HAMMER)
                .saveSuffixed(output, "_with_manual")
        }
    }

    @JvmStatic
    private fun baseToBlock() {
        for ((basePrefix: HTMaterialPrefix, material: HCMaterial) in HCMaterial.getPrefixedEntries()) {
            val block: ItemLike = HCBlocks.MATERIALS[HCMaterialPrefixes.STORAGE_BLOCK, material] ?: continue
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
    private fun dustOrRawToBase() {
        for ((basePrefix: HTMaterialPrefix, material: HCMaterial) in HCMaterial.getPrefixedEntries()) {
            if (basePrefix == HCMaterialPrefixes.DUST) continue
            val prefixMap: Map<HTMaterialPrefix, HTSimpleDeferredItem> = HCItems.MATERIALS.column(material)

            val base: HTSimpleDeferredItem = prefixMap[basePrefix] ?: continue
            val items: List<HTSimpleDeferredItem> = arrayOf(
                HCMaterialPrefixes.DUST,
                HCMaterialPrefixes.RAW_MATERIAL,
            ).mapNotNull(prefixMap::get)
            if (items.isEmpty()) continue

            // Smelting & Blasting
            HTCookingRecipeBuilder.smeltingAndBlasting(base) {
                addIngredient(items)
                setExp(0.5f)
                save(output)
            }
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
