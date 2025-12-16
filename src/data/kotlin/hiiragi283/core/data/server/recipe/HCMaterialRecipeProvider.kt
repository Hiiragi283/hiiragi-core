package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.common.data.recipe.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.HCMoltenCrystalData
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.common.tag.HCCommonTags
import hiiragi283.core.common.tag.HCModTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HCMaterialRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Cryogen Powder
        HTShapelessRecipeBuilder
            .create(HCItems.CRYOGEN_POWDER, 2)
            .addIngredient(Items.SNOWBALL)
            .addIngredient(Items.WIND_CHARGE)
            .addIngredient(Items.PACKED_ICE)
            .save(output)

        manual()
        buckets()

        baseToDust()
        baseToBlock()

        dustOrRawToBase()

        ingotToGear()
        ingotToNugget()

        rawToDust()
    }

    @JvmStatic
    private fun manual() {
        // Amethyst + Lapis -> Azure Shard
        HTShapelessRecipeBuilder
            .create(HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, HCMaterial.Gems.AZURE), 4)
            .addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Gems.AMETHYST, 2)
            .addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Gems.LAPIS, 2)
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
    }

    @JvmStatic
    private fun buckets() {
        // Honey Bottle <-> Honey Bucket
        HTShapelessRecipeBuilder
            .create(HCFluids.HONEY.bucket)
            .addIngredients(Tags.Items.DRINKS_HONEY, 4)
            .addIngredient(Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_from_bottles")
        HTShapelessRecipeBuilder
            .create(HCFluids.HONEY.bucket)
            .addIngredient(Items.HONEY_BLOCK)
            .addIngredient(Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_from_block")

        HTShapelessRecipeBuilder
            .create(Items.HONEY_BOTTLE, 4)
            .addIngredient(HCFluids.HONEY.bucketTag)
            .addIngredients(Items.GLASS_BOTTLE, count = 4)
            .saveSuffixed(output, "_from_bucket")
        HTShapelessRecipeBuilder
            .create(Items.HONEY_BLOCK)
            .addIngredient(HCFluids.HONEY.bucketTag)
            .saveSuffixed(output, "_from_bucket")
        // Mushroom Stew
        HTShapelessRecipeBuilder
            .create(HCFluids.MUSHROOM_STEW.bucket)
            .addIngredients(Items.MUSHROOM_STEW, count = 4)
            .addIngredient(Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_from_bowls")

        // Latex
        HTShapedRecipeBuilder
            .create(HCFluids.LATEX.bucket)
            .hollow8()
            .define('A', Items.DANDELION)
            .define('B', Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_from_flower")
        // Latex -> Raw Rubber
        HTShapelessRecipeBuilder
            .create(HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER))
            .addIngredient(HCFluids.LATEX.bucketTag)
            .saveSuffixed(output, "_from_bucket")
        // Raw Rubber + Sulfur + Coal -> Rubber
        HTShapelessRecipeBuilder
            .create(HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.PLATE, HCMaterial.Plates.RUBBER), 3)
            .addIngredient(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER)
            .addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Gems.SULFUR)
            .addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Fuels.COAL, HCMaterial.Fuels.CHARCOAL)
            .addIngredient(HCItems.MAGMA_SHARD)
            .savePrefixed(output, "black_")

        // Molten
        for (data: HCMoltenCrystalData in HCMoltenCrystalData.entries) {
            // Molten -> Gem
            val molten: HTSimpleFluidContent = data.molten
            val material: HCMaterial = data.material
            HTShapelessRecipeBuilder
                .create(HCItems.MATERIALS.getOrThrow(material.basePrefix, material))
                .addIngredient(molten.bucketTag)
                .addIngredient(HCItems.CRYOGEN_POWDER)
                .saveSuffixed(output, "_from_molten")
            // Sap -> Molten
            val sap: HTSimpleFluidContent = data.sap ?: continue
            HTShapelessRecipeBuilder
                .create(molten.bucket)
                .addIngredients(sap.bucketTag, 2)
                .addIngredient(Tags.Items.BUCKETS_EMPTY)
                .addIngredient(HCItems.MAGMA_SHARD)
                .saveSuffixed(output, "_from_sap")
            // Log -> Sap
            val log: TagKey<Item> = data.base ?: continue
            HTShapelessRecipeBuilder
                .create(sap.bucket)
                .addIngredients(log, 4)
                .addIngredient(Tags.Items.BUCKETS_EMPTY)
                .addIngredient(HCCommonTags.Items.TOOLS_HAMMER)
                .saveSuffixed(output, "_from_log")
        }

        // Eldritch
        HTShapelessRecipeBuilder
            .create(HCFluids.ELDRITCH_FLUX.bucket)
            .addIngredient(HCFluids.CRIMSON_BLOOD.bucketTag)
            .addIngredient(HCFluids.DEW_OF_THE_WARP.bucketTag)
            .addIngredient(HCModTags.Items.ELDRITCH_PEARL_BINDER)
            .addIngredient(Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_from_mix")
    }

    @JvmStatic
    private fun baseToDust() {
        for (material: HCMaterial in HCMaterial.entries) {
            val dust: HTSimpleDeferredItem = HCItems.MATERIALS[HCMaterialPrefixes.DUST, material] ?: continue
            // Crushing
            HTSingleItemRecipeBuilder
                .crushing(dust)
                .addIngredient(material.getBaseIngredient())
                .saveSuffixed(output, "_from_base")
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

    @JvmStatic
    fun rawToDust() {
        for (material: HCMaterial in HCMaterial.entries) {
            if (HCMaterialPrefixes.RAW_MATERIAL !in material.getSupportedItemPrefixes()) continue
            val dust: HTSimpleDeferredItem = HCItems.MATERIALS[HCMaterialPrefixes.DUST, material] ?: continue
            // Crushing
            HTSingleItemRecipeBuilder
                .crushing(dust, 12)
                .addIngredient(HCMaterialPrefixes.RAW_MATERIAL, material)
                .setExp(1.2f)
                .saveSuffixed(output, "_from_raw_block")
        }
    }
}
