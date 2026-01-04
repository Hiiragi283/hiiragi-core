package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.crafting.HTEternalUpgradeRecipe
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialItems
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.core.component.DataComponentPredicate
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

object HCMaterialRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Compressed Sawdust -> Charcoal
        HTCookingRecipeBuilder
            .smelting(Items.CHARCOAL)
            .addIngredient(HCItems.COMPRESSED_SAWDUST)
            .setTime(20 * 30)
            .setExp(0.5f)
            .saveSuffixed(output, "_from_sawdust")
        // Wheat Dough
        HTShapelessRecipeBuilder
            .create(HCItems.WHEAT_DOUGH)
            .addIngredient(HiiragiCoreTags.Items.FLOURS_WHEAT)
            .addIngredient(
                DataComponentIngredient.of(
                    false,
                    DataComponentPredicate
                        .builder()
                        .expect(DataComponents.POTION_CONTENTS, PotionContents(Potions.WATER))
                        .build(),
                    Items.POTION,
                ),
            ).saveSuffixed(output, "_with_bottle")

        HTShapelessRecipeBuilder
            .create(HCItems.WHEAT_DOUGH, 3)
            .addIngredients(HiiragiCoreTags.Items.FLOURS_WHEAT, 3)
            .addIngredient(Tags.Items.BUCKETS_WATER)
            .saveSuffixed(output, "_with_bucket")
        // Bread
        HTCookingRecipeBuilder.smeltingAndSmoking(Items.BREAD) {
            addIngredient(HCItems.WHEAT_DOUGH)
            setExp(0.3f)
            saveSuffixed(output, "_from_dough")
        }
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

        // Eternal Ticket
        save(id("shapeless", "eternal_upgrade"), HTEternalUpgradeRecipe(CraftingBookCategory.EQUIPMENT))

        manual()
        buckets()
    }

    @JvmStatic
    private fun manual() {
        // Amethyst + Lapis -> Azure Shard
        addManualSmelting(getItemOrThrow(HCMaterialPrefixes.GEM, HCMaterial.Gems.AZURE), 2) {
            addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Gems.AMETHYST)
            addIngredient(HCMaterialPrefixes.DUST, HCMaterial.Gems.LAPIS)
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
    }

    @JvmStatic
    private fun getItemOrThrow(prefix: HTPrefixLike, material: HTMaterialLike): HTItemHolderLike<*> =
        HCItems.MATERIALS[prefix, material] ?: VanillaMaterialItems.MATERIALS.getOrThrow(prefix, material)
}
