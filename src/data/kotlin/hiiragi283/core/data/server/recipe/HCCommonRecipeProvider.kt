package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HTConst
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
import hiiragi283.core.common.data.recipe.builder.HTStonecuttingRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
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

object HCCommonRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Bamboo -> Bamboo Charcoal
        HTCookingRecipeBuilder
            .smelting(HCItems.BAMBOO_CHARCOAL)
            .addIngredient(Items.BAMBOO)
            .setExp(0.5f)
            .save(output)
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

        // Slot Cover
        HTStonecuttingRecipeBuilder
            .create(HCItems.SLOT_COVER, 3)
            .addIngredient(Items.SMOOTH_STONE_SLAB)
            .save(output)
        // Trader Catalog
        HTShapelessRecipeBuilder
            .create(HCItems.TRADER_CATALOG)
            .addIngredient(Items.BOOK)
            .addIngredient(HCMaterialPrefixes.GEM, VanillaMaterialKeys.EMERALD)
            .setCategory(CraftingBookCategory.EQUIPMENT)
            .save(output)
        // Eldritch Egg
        HTShapedRecipeBuilder
            .create(HCItems.ELDRITCH_EGG, 4)
            .hollow4()
            .define('A', Tags.Items.EGGS)
            .define('B', HCMaterialPrefixes.PEARL, HCMaterialKeys.ELDRITCH)
            .setCategory(CraftingBookCategory.EQUIPMENT)
            .save(output)

        // Eternal Ticket
        save(id(HTConst.SHAPELESS, "eternal_upgrade"), HTEternalUpgradeRecipe(CraftingBookCategory.EQUIPMENT))

        manual()
        buckets()
    }

    @JvmStatic
    private fun manual() {
        // Amethyst + Lapis -> Azure Shard
        addManualSmelting(getItemOrThrow(HCMaterialPrefixes.GEM, HCMaterialKeys.AZURE), 2) {
            addIngredient(HCMaterialPrefixes.DUST, VanillaMaterialKeys.AMETHYST)
            addIngredient(HCMaterialPrefixes.DUST, VanillaMaterialKeys.LAPIS)
        }

        // Gold + Obsidian + Blackstone -> Night Metal
        addManualSmelting(getItemOrThrow(HCMaterialPrefixes.INGOT, HCMaterialKeys.NIGHT_METAL)) {
            addIngredient(HCMaterialPrefixes.DUST, VanillaMaterialKeys.GOLD)
            addIngredient(HCMaterialPrefixes.DUST, VanillaMaterialKeys.OBSIDIAN)
            addIngredients(Items.BLACKSTONE, count = 4)
        }

        // Netherite Scrap + Azure Steel -> Deep Steel
        addManualSmelting(getItemOrThrow(HCMaterialPrefixes.INGOT, VanillaMaterialKeys.NETHERITE)) {
            addIngredients(HCMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 4)
            addIngredients(HCMaterialPrefixes.DUST, VanillaMaterialKeys.GOLD, 4)
        }
        // Azure Shard + Iron -> Azure Steel
        addManualSmelting(getItemOrThrow(HCMaterialPrefixes.INGOT, HCMaterialKeys.AZURE_STEEL)) {
            addIngredients(HCMaterialPrefixes.DUST, HCMaterialKeys.AZURE, 2)
            addIngredient(HCMaterialPrefixes.DUST, VanillaMaterialKeys.IRON)
        }
        // Deep Scrap + Azure Steel -> Deep Steel
        addManualSmelting(getItemOrThrow(HCMaterialPrefixes.INGOT, HCMaterialKeys.DEEP_STEEL)) {
            addIngredients(HCMaterialPrefixes.SCRAP, HCMaterialKeys.DEEP_STEEL, 4)
            addIngredients(HCMaterialPrefixes.DUST, HCMaterialKeys.AZURE_STEEL, 4)
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
            .create(getItemOrThrow(HCMaterialPrefixes.RAW_MATERIAL, CommonMaterialKeys.RUBBER))
            .addIngredient(HCFluids.LATEX.bucketTag)
            .saveSuffixed(output, "_from_bucket")
    }

    @JvmStatic
    private fun getItemOrThrow(prefix: HTPrefixLike, material: HTMaterialLike): HTItemHolderLike<*> =
        HCItems.MATERIALS[prefix, material] ?: VanillaMaterialKeys.INGREDIENTS.getOrThrow(prefix, material)
}
