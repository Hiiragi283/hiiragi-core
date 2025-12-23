package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialItems
import hiiragi283.core.common.tag.HCModTags
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.tags.ItemTags
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

        // Wither Doll
        HTShapedRecipeBuilder
            .create(HCItems.WITHER_DOLL)
            .pattern(
                "AAA",
                "BBB",
                " B ",
            ).define('A', Items.WITHER_SKELETON_SKULL)
            .define('B', ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .save(output)

        manual()
        buckets()
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
            addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Obsidian)
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
        // Exp Bottle <-> Exp Bucket
        HTShapelessRecipeBuilder
            .create(HCFluids.EXPERIENCE.bucket)
            .addIngredients(Items.EXPERIENCE_BOTTLE, count = 4)
            .addIngredient(Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_from_bottles")
        HTShapelessRecipeBuilder
            .create(Items.EXPERIENCE_BOTTLE, 4)
            .addIngredient(HCFluids.EXPERIENCE.bucketTag)
            .addIngredients(Items.GLASS_BOTTLE, count = 4)
            .saveSuffixed(output, "_from_bucket")

        // Honey Bottle <-> Honey Bucket
        HTShapelessRecipeBuilder
            .create(HCFluids.HONEY.bucket)
            .addIngredients(Tags.Items.DRINKS_HONEY, 4)
            .addIngredient(Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_from_bottles")
        HTShapelessRecipeBuilder
            .create(Items.HONEY_BOTTLE, 4)
            .addIngredient(HCFluids.HONEY.bucketTag)
            .addIngredients(Items.GLASS_BOTTLE, count = 4)
            .saveSuffixed(output, "_from_bucket")
        // Honey Block <-> Honey Bucket
        HTShapelessRecipeBuilder
            .create(HCFluids.HONEY.bucket)
            .addIngredient(Items.HONEY_BLOCK)
            .addIngredient(Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_from_block")
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
            .addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SULFUR)
            .addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Fuels.COAL, HCMaterial.Fuels.CHARCOAL)
            .addIngredient(Items.FIRE_CHARGE)
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
    private fun getItemOrThrow(prefix: HTPrefixLike, material: HTMaterialLike): HTItemHolderLike<*> =
        HCItems.MATERIALS[prefix, material] ?: VanillaMaterialItems.MATERIALS.getOrThrow(prefix, material)
}
