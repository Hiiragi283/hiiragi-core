package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.data.recipe.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialItems
import hiiragi283.core.common.tag.HCModTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HCMaterialRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Compressed Sawdust -> Charcoal
        HTCookingRecipeBuilder
            .smelting(Items.CHARCOAL)
            .addIngredient(HCItems.COMPRESSED_SAWDUST)
            .setTime(20 * 30)
            .setExp(0.5f)
            .saveSuffixed(output, "_from_sawdust")

        manual()
        buckets()

        baseToBlock()

        prefixToBase(HCMaterialPrefixes.DUST, 0.35f)
        prefixToBase(HCMaterialPrefixes.RAW_MATERIAL, 0.7f)

        ingotToGear()
        ingotToNugget()
    }

    @JvmStatic
    private fun manual() {
        // Amethyst + Lapis -> Azure Shard
        addManualSmelting(getItemOrThrow(HCMaterialPrefixes.GEM, HCMaterial.Gems.AZURE), 4) {
            addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Gems.AMETHYST, 2)
            addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Gems.LAPIS, 2)
        }

        // Gold + Obsidian + Blackstone -> Night Metal
        addManualSmelting(getItemOrThrow(HCMaterialPrefixes.INGOT, HCMaterial.Metals.NIGHT_METAL)) {
            addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Metals.GOLD)
            addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Dusts.OBSIDIAN)
            addIngredients(Items.BLACKSTONE, count = 4)
        }

        // Netherite Scrap + Azure Steel -> Deep Steel
        addManualSmelting(getItemOrThrow(HCMaterialPrefixes.INGOT, HCMaterial.Alloys.NETHERITE)) {
            addIngredients(HCMaterialPrefixes.SCRAP, HCMaterial.Alloys.NETHERITE, 4)
            addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Metals.GOLD, 4)
        }
        // Azure Shard + Iron -> Azure Steel
        addManualSmelting(getItemOrThrow(HCMaterialPrefixes.INGOT, HCMaterial.Alloys.AZURE_STEEL)) {
            addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Gems.AZURE, 2)
            addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Metals.IRON)
        }
        // Deep Scrap + Azure Steel -> Deep Steel
        addManualSmelting(getItemOrThrow(HCMaterialPrefixes.INGOT, HCMaterial.Alloys.DEEP_STEEL)) {
            addIngredients(HCMaterialPrefixes.SCRAP, HCMaterial.Alloys.DEEP_STEEL, 4)
            addIngredients(HCMaterialPrefixes.DUST, HCMaterial.Alloys.AZURE_STEEL, 4)
        }
    }

    @JvmStatic
    private inline fun addManualSmelting(result: ItemLike, count: Int = 1, builderAction: HTShapelessRecipeBuilder.() -> Unit) {
        HTShapelessRecipeBuilder
            .create(result, count)
            .apply(builderAction)
            .addIngredient(Items.FIRE_CHARGE)
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
            .create(getItemOrThrow(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER))
            .addIngredient(HCFluids.LATEX.bucketTag)
            .saveSuffixed(output, "_from_bucket")
        // Raw Rubber + Sulfur + Coal -> Rubber
        HTShapelessRecipeBuilder
            .create(getItemOrThrow(HCMaterialPrefixes.PLATE, HCMaterial.Plates.RUBBER), 3)
            .addIngredient(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER)
            .addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Gems.SULFUR)
            .addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Fuels.COAL, HCMaterial.Fuels.CHARCOAL)
            .addIngredient(HCItems.MAGMA_SHARD)
            .savePrefixed(output, "black_")
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
    private fun baseToBlock() {
        for ((basePrefix: HTMaterialPrefix, material: HCMaterial) in HCMaterial.getPrefixedEntries()) {
            val block: ItemLike = HCBlocks.MATERIALS[HCMaterialPrefixes.STORAGE_BLOCK, material] ?: continue
            val base: ItemLike = getItem(basePrefix, material) ?: continue
            // Shapeless
            HTShapelessRecipeBuilder
                .create(base, 9)
                .addIngredient(HCMaterialPrefixes.STORAGE_BLOCK, material)
                .save(output, HiiragiCoreAPI.id(material.asMaterialName(), "${basePrefix.name}_from_block"))
            // Shaped
            HTShapedRecipeBuilder
                .create(block)
                .hollow8()
                .define('A', basePrefix, material)
                .define('B', base)
                .save(output, HiiragiCoreAPI.id(material.asMaterialName(), "block_from_${basePrefix.name}"))
        }
    }

    @JvmStatic
    private fun prefixToBase(prefix: HTPrefixLike, exp: Float) {
        for ((basePrefix: HTMaterialPrefix, material: HCMaterial) in HCMaterial.getPrefixedEntries()) {
            if (material is HCMaterial.Fuels || material is HCMaterial.Dusts) continue
            val prefixMap: Map<HTMaterialPrefix, HTItemHolderLike<*>> = getPrefixMap(material)

            val base: HTItemHolderLike<*> = prefixMap[basePrefix] ?: continue
            val input: HTItemHolderLike<*> = prefixMap[prefix] ?: continue
            if (base.getNamespace() == HTConst.MINECRAFT && input.getNamespace() == HTConst.MINECRAFT) continue

            // Smelting & Blasting
            HTCookingRecipeBuilder.smeltingAndBlasting(base) {
                addIngredient(input)
                setExp(exp)
                save(
                    output,
                    HiiragiCoreAPI.id(
                        material.asMaterialName(),
                        "${basePrefix.asPrefixName()}_from_${prefix.asPrefixName()}",
                    ),
                )
            }
        }
    }

    @JvmStatic
    private fun ingotToGear() {
        for ((key: HTMaterialKey, gear: HTItemHolderLike<*>) in HCItems.MATERIALS.row(HCMaterialPrefixes.GEAR)) {
            // Shaped
            HTShapedRecipeBuilder
                .create(gear)
                .hollow4()
                .define('A', HCMaterialPrefixes.INGOT, key)
                .define('B', HCMaterialPrefixes.NUGGET, HCMaterial.Metals.IRON)
                .save(output, HiiragiCoreAPI.id(key.name, "gear"))
        }
    }

    @JvmStatic
    fun ingotToNugget() {
        for ((key: HTMaterialKey, nugget: HTItemHolderLike<*>) in HCItems.MATERIALS.row(HCMaterialPrefixes.NUGGET)) {
            // Shapeless
            HTShapelessRecipeBuilder
                .create(nugget, 9)
                .addIngredient(HCMaterialPrefixes.INGOT, key)
                .save(output, HiiragiCoreAPI.id(key.name, "nugget_from_ingot"))
            // Shaped
            val ingot: HTItemHolderLike<*> = getItem(HCMaterialPrefixes.INGOT, key) ?: continue
            HTShapedRecipeBuilder
                .create(ingot)
                .hollow8()
                .define('A', HCMaterialPrefixes.NUGGET, key)
                .define('B', nugget)
                .save(output, HiiragiCoreAPI.id(key.name, "ingot_from_nugget"))
        }
    }

    @JvmStatic
    private fun getItem(prefix: HTPrefixLike, material: HTMaterialLike): HTItemHolderLike<*>? =
        HCItems.MATERIALS[prefix, material] ?: VanillaMaterialItems.MATERIALS[prefix, material]

    @JvmStatic
    private fun getItemOrThrow(prefix: HTPrefixLike, material: HTMaterialLike): HTItemHolderLike<*> =
        HCItems.MATERIALS[prefix, material] ?: VanillaMaterialItems.MATERIALS.getOrThrow(prefix, material)

    @JvmStatic
    private fun getPrefixMap(material: HTMaterialLike): Map<HTMaterialPrefix, HTItemHolderLike<*>> = buildMap {
        // Hiiragi Core
        for ((prefix: HTMaterialPrefix, item: HTItemHolderLike<*>) in HCItems.MATERIALS.column(material)) {
            put(prefix, item)
        }
        // Vanilla
        for ((prefix: HTMaterialPrefix, item: HTItemHolderLike<*>) in VanillaMaterialItems.MATERIALS.column(material)) {
            put(prefix, item)
        }
    }
}
