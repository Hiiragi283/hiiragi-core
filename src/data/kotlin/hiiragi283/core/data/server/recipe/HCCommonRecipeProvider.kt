package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.crafting.HTEternalSmithingRecipe
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTStonecuttingRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags

object HCCommonRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        materials()
        utilities()
        buckets()
    }

    @JvmStatic
    private fun materials() {
        // Sand + Ash -> Glass Dust
        HTShapelessRecipeBuilder
            .create(getOrThrow(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLASS), 4)
            .addIngredients(Tags.Items.SANDS, 3)
            .addIngredient(CommonTagPrefixes.DUST, CommonMaterialKeys.ASH)
            .saveSuffixed(output, "_from_sand_and_ash")
        // Iron Rod -> Iron Bar
        HTShapedRecipeBuilder
            .create(Items.IRON_BARS, 8)
            .pattern(
                "AAA",
                "AAA",
            ).define('A', CommonTagPrefixes.ROD, VanillaMaterialKeys.IRON)
            .saveSuffixed(output, "_from_rod")
        // Compressed Sawdust -> Charcoal
        HTCookingRecipeBuilder
            .smelting(Items.CHARCOAL)
            .addIngredient(HCItems.COMPRESSED_SAWDUST)
            .setTime(20 * 30)
            .setExp(0.5f)
            .saveSuffixed(output, "_from_sawdust")
        // Dough -> Bread
        HTCookingRecipeBuilder.smeltingAndSmoking(Items.BREAD) {
            addIngredient(getOrThrow(CommonTagPrefixes.DOUGH, VanillaMaterialKeys.WHEAT))
            setExp(0.3f)
            saveSuffixed(output, "_from_dough")
        }

        // Bamboo -> Bamboo Charcoal
        HTCookingRecipeBuilder
            .smelting(HCItems.BAMBOO_CHARCOAL)
            .addIngredient(Items.BAMBOO)
            .setExp(0.5f)
            .save(output)
        // Polymer Resin -> Plastic Plate
        HTCookingRecipeBuilder
            .smelting(getOrThrow(CommonTagPrefixes.PLATE, CommonMaterialKeys.PLASTIC))
            .addIngredient(HCItems.POLYMER_RESIN)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_resin")
        // Steel Compound
        HTShapelessRecipeBuilder
            .create(HCItems.STEEL_COMPOUND)
            .addIngredient(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
            .addIngredients(CommonTagPrefixes.DUST, VanillaMaterialKeys.CHARCOAL, 2)
            .saveSuffixed(output, "_with_charcoal")

        HTShapelessRecipeBuilder
            .create(HCItems.STEEL_COMPOUND)
            .addIngredient(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
            .addIngredients(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL, 4)
            .saveSuffixed(output, "_with_coal")

        HTCookingRecipeBuilder
            .blasting(getOrThrow(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL))
            .addIngredient(HCItems.STEEL_COMPOUND)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_compound")
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
    }

    @JvmStatic
    private fun utilities() {
        // Slot Cover
        HTStonecuttingRecipeBuilder
            .create(HCItems.SLOT_COVER, 3)
            .addIngredient(Items.SMOOTH_STONE_SLAB)
            .save(output)
        // Trader Catalog
        HTShapelessRecipeBuilder
            .create(HCItems.TRADER_CATALOG)
            .addIngredient(Items.BOOK)
            .addIngredient(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD)
            .setCategory(CraftingBookCategory.EQUIPMENT)
            .save(output)
        // Eldritch Egg
        HTShapedRecipeBuilder
            .create(HCItems.ELDRITCH_EGG, 4)
            .hollow4()
            .define('A', Tags.Items.EGGS)
            .define('B', CommonTagPrefixes.PEARL, HCMaterialKeys.ELDRITCH)
            .setCategory(CraftingBookCategory.EQUIPMENT)
            .save(output)

        // Eternal Upgrade
        HTShapedRecipeBuilder
            .create(HCItems.ETERNAL_UPGRADE)
            .pattern(
                "ABA",
                "ACA",
                "AAA",
            ).define('A', CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            .define('B', HCItems.ETERNAL_UPGRADE)
            .define('C', HCItems.IRIDESCENT_POWDER)
            .save(output)
        save(id(HTConst.SMITHING, "eternal_upgrade"), HTEternalSmithingRecipe)
        // Almighty Pickaxe
        HTShapelessRecipeBuilder
            .create(HCItems.ALMIGHTY_PICKAXE)
            .addIngredient(Items.NETHERITE_SHOVEL)
            .addIngredient(Items.NETHERITE_PICKAXE)
            .addIngredient(Items.NETHERITE_AXE)
            .addIngredient(Items.NETHERITE_HOE)
            .addIngredients(CommonTagPrefixes.INGOT, CommonMaterialKeys.IRIDIUM, 4)
            .save(output)
    }

    @JvmStatic
    private fun buckets() {
        // Exp Bottle <-> Exp Bucket
        HTShapelessRecipeBuilder
            .create(HCFluids.EXPERIENCE.getBucket())
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
            .create(HCFluids.HONEY.getBucket())
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
            .create(HCFluids.HONEY.getBucket())
            .addIngredient(Items.HONEY_BLOCK)
            .addIngredient(Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_from_block")
        HTShapelessRecipeBuilder
            .create(Items.HONEY_BLOCK)
            .addIngredient(HCFluids.HONEY.bucketTag)
            .saveSuffixed(output, "_from_bucket")
        // Mushroom Stew
        HTShapelessRecipeBuilder
            .create(HCFluids.MUSHROOM_STEW.getBucket())
            .addIngredients(Items.MUSHROOM_STEW, count = 4)
            .addIngredient(Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_from_bowls")

        // Latex
        HTShapedRecipeBuilder
            .create(HCFluids.LATEX.getBucket())
            .hollow8()
            .define('A', Items.DANDELION)
            .define('B', Tags.Items.BUCKETS_EMPTY)
            .saveSuffixed(output, "_from_flower")
        // Latex -> Raw Rubber
        HTShapelessRecipeBuilder
            .create(HCItems.RAW_RUBBER)
            .addIngredient(HCFluids.LATEX.bucketTag)
            .saveSuffixed(output, "_from_bucket")
        // Raw Rubber -> Rubber Plate
        HTCookingRecipeBuilder
            .smelting(getOrThrow(CommonTagPrefixes.PLATE, CommonMaterialKeys.RUBBER))
            .addIngredient(HCItems.RAW_RUBBER)
            .setExp(0.7f)
            .saveSuffixed(output, "_from_raw")
    }

    @JvmStatic
    private fun getOrThrow(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*> =
        HTMaterialContentsAccess.INSTANCE.getItemOrThrow(prefix, material)
}
