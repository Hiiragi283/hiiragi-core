package hiiragi283.core.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.item.toStack
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.times
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.common.crafting.HCExperienceStoringRecipe
import hiiragi283.core.common.crafting.HTBlueprintCloningRecipe
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTStonecuttingRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.WeatheringCopper
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import org.apache.commons.lang3.math.Fraction

object HCCommonRecipeProvider : HTSubRecipeProvider.Direct(HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        vanilla()
        materials()
        utilities()
        buckets()

        copper()
    }

    @JvmStatic
    private fun vanilla() {
        // Sand + Ash -> Glass Dust
        HTShapelessRecipeBuilder.create(output) {
            repeat(3) {
                ingredients += itemCreator.create(Tags.Items.SANDS)
            }
            ingredients += itemCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.ASH)
            resultStack = getOrThrow(CommonParts.DUST, VanillaMaterialKeys.GLASS).toStack(4)
            recipeId suffix "_from_sand_and_ash"
        }
        // Glass Dust -> Glass
        HTCookingRecipeBuilder.smelting(output) {
            ingredient = itemCreator.create(getOrThrow(CommonParts.DUST, VanillaMaterialKeys.GLASS).get())
            resultStack = ItemStack(Items.GLASS)
            recipeId suffix "_from_dust"
        }
        // Iron Rod -> Iron Bar
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "AAA",
            )
            define('A') { itemCreator.create(CommonTagPrefixes.ROD, VanillaMaterialKeys.IRON) }
            resultStack = ItemStack(Items.IRON_BARS, 8)
            recipeId suffix "_from_rod"
        }
        // Sticky Piston
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(HiiragiCoreTags.Items.STICKY_BALLS)
            ingredients += itemCreator.create(Items.PISTON)
            resultStack = ItemStack(Items.STICKY_PISTON)
        }

        // Steel + Flint
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL)
            ingredients += itemCreator.create(Items.FLINT)
            resultStack = createItemStack(Items.FLINT_AND_STEEL, DataComponents.MAX_DAMAGE, 64 * 3)
            recipeId replace id("real_flint_and_steel")
        }
    }

    @JvmStatic
    private fun materials() {
        // Bamboo -> Bamboo Charcoal
        HTCookingRecipeBuilder.smelting(output) {
            ingredient = itemCreator.create(Items.BAMBOO)
            resultStack = HCItems.BAMBOO_CHARCOAL.toStack()
            exp = 0.5f
        }
        // Compressed Sawdust -> Particle Board
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') { itemCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD) }
            define('B') { itemCreator.create(HiiragiCoreTags.Items.STICKY_BALLS) }
            resultStack = HCItems.PARTICLE_BOARD.toStack(4)
        }

        // Steel Compound
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
            repeat(2) {
                ingredients += itemCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.CHARCOAL)
            }
            resultStack = HCItems.STEEL_COMPOUND.toStack()
            recipeId suffix "_with_charcoal"
        }
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
            repeat(4) {
                ingredients += itemCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL)
            }
            resultStack = HCItems.STEEL_COMPOUND.toStack()
            recipeId suffix "_with_coal"
        }
        HTCookingRecipeBuilder.blasting(output) {
            ingredient = itemCreator.create(HCItems.STEEL_COMPOUND)
            resultStack = getOrThrow(CommonParts.INGOT, CommonMaterialKeys.STEEL).toStack()
            exp = 0.7f
            recipeId suffix "_from_compound"
        }

        // Polymer Resin -> Plastic Bar
        HTCookingRecipeBuilder.smelting(output) {
            ingredient = itemCreator.create(HCItems.POLYMER_RESIN)
            resultStack = getOrThrow(CommonParts.PLATE, CommonMaterialKeys.PLASTIC).toStack()
            exp = 0.7f
            recipeId suffix "_from_resin"
        }
        // Synthetic
        for (item: HTSimpleDeferredItem in listOf(HCItems.SYNTHETIC_FEATHER, HCItems.SYNTHETIC_FIBER, HCItems.SYNTHETIC_LEATHER)) {
            HTStonecuttingRecipeBuilder.create(output) {
                ingredient = itemCreator.create(HiiragiCoreTags.Items.PLASTICS)
                resultStack = item.toStack()
            }
        }

        // Warped Wart Block
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') { itemCreator.create(HiiragiCoreTags.Items.CROPS_WARPED_WART) }
            define('B') { itemCreator.create(HCBlocks.WARPED_WART) }
            resultStack = ItemStack(Items.WARPED_WART_BLOCK)
        }
        // Flour + Water -> Dough
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(HiiragiCoreTags.Items.FLOURS_WHEAT)
            ingredients += DataComponentIngredient.of(
                false,
                DataComponents.POTION_CONTENTS,
                PotionContents(Potions.WATER),
                Items.POTION,
            )
            resultStack = HCItems.WHEAT_DOUGH.toStack()
            recipeId suffix "_with_bottle"
        }
        HTShapelessRecipeBuilder.create(output) {
            repeat(3) {
                ingredients += itemCreator.create(HiiragiCoreTags.Items.FLOURS_WHEAT)
            }
            ingredients += itemCreator.create(Tags.Items.BUCKETS_WATER)
            resultStack = HCItems.WHEAT_DOUGH.toStack(3)
            recipeId suffix "_with_bucket"
        }
        // Dough -> Bread
        HTCookingRecipeBuilder.smeltingAndSmoking(output) {
            ingredient = itemCreator.create(HCItems.WHEAT_DOUGH)
            resultStack = ItemStack(Items.BREAD)
            exp = 0.3f
            recipeId suffix "_from_dough"
        }

        // Wither Doll
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "BBB",
                " B ",
            )
            define('A') { itemCreator.create(Items.WITHER_SKELETON_SKULL) }
            define('B') { itemCreator.create(ItemTags.SOUL_FIRE_BASE_BLOCKS) }
            resultStack = HCItems.WITHER_DOLL.toStack()
        }

        registerIronAlt(CommonMaterialKeys.BRONZE, fraction(3, 2))
        registerIronAlt(CommonMaterialKeys.BRASS, fraction(3, 2))
        registerIronAlt(CommonMaterialKeys.STEEL, fraction(2))
        registerIronAlt(CommonMaterialKeys.INVAR, fraction(2))
    }

    @JvmStatic
    private fun registerIronAlt(key: HTMaterialKey, multiplier: Fraction) {
        val suffix: String = key.path
        val entry: Pair<HTTagPrefix, HTMaterialKey> = CommonTagPrefixes.INGOT to key
        val ingredient: Ingredient = itemCreator.create(CommonTagPrefixes.INGOT, key)
        // Bucket
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "A A",
                " A ",
            )
            define('A') { ingredient }
            resultStack = ItemStack(Items.BUCKET, multiplier.toInt())
            recipeId suffix "_from_$suffix"
            conditions += entry
        }
        // Hopper
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "A A",
                "ABA",
                " A ",
            )
            define('A') { ingredient }
            define('B') { itemCreator.create(Tags.Items.CHESTS) }
            resultStack = ItemStack(Items.HOPPER, multiplier.toInt())
            recipeId suffix "_from_$suffix"
            conditions += entry
        }
        // Piston
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "BCB",
                "BDB",
            )
            define('A') { itemCreator.create(ItemTags.PLANKS) }
            define('B') { itemCreator.create(ItemTags.STONE_CRAFTING_MATERIALS) }
            define('C') { ingredient }
            define('D') { itemCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE) }
            resultStack = ItemStack(Items.PISTON, multiplier.toInt())
            recipeId suffix "_from_$suffix"
            conditions += entry
        }
        // Rail
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "A A",
                "ABA",
                "A A",
            )
            define('A') { ingredient }
            define('B') { itemCreator.create(Tags.Items.RODS_WOODEN) }
            resultStack = ItemStack(Items.RAIL, (16 * multiplier).toInt())
            recipeId suffix "_from_$suffix"
            conditions += entry
        }
    }

    @JvmStatic
    private fun utilities() {
        // Paint Brush
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "A",
                "B",
                "C",
            )
            define('A') { itemCreator.create(ItemTags.WOOL) }
            define('B') { itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER) }
            define('C') { itemCreator.create(Tags.Items.RODS_WOODEN) }
            resultStack = HCItems.PAINT_BRUSH.toStack()
        }

        // Blueprint
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(Items.PAPER)
            ingredients += itemCreator.create(Tags.Items.DYES_WHITE)
            ingredients += itemCreator.create(Tags.Items.DYES_BLUE)
            resultStack = HCItems.BLUEPRINT.toStack()
        }
        save(id(HTConst.SHAPELESS, "blueprint_cloning"), HTBlueprintCloningRecipe(CraftingBookCategory.MISC))
        // Bomb
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "  A",
                "BC ",
                "CB ",
            )
            define('A') { itemCreator.create(Tags.Items.STRINGS) }
            define('B') { itemCreator.create(CommonTagPrefixes.NUGGET, VanillaMaterialKeys.IRON) }
            define('C') { itemCreator.create(Tags.Items.GUNPOWDERS) }
            resultStack = HCItems.BOMB.toStack()
        }
        // Slot Cover
        HTStonecuttingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(Items.SMOOTH_STONE_SLAB)
            resultStack = HCItems.SLOT_COVER.toStack(3)
        }
        // Trader Catalog
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(Items.BOOK)
            ingredients += itemCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD)
            resultStack = HCItems.TRADER_CATALOG.toStack()
        }
        // Eldritch Egg
        HTShapedRecipeBuilder.create(output) {
            hollow4()
            define('A') { itemCreator.create(Tags.Items.EGGS) }
            define('B') { itemCreator.create(CommonTagPrefixes.PEARL, HCMaterialKeys.ELDRITCH) }
            resultStack = HCItems.ELDRITCH_EGG.toStack(4)
        }
        // Experience Tome
        HTShapedRecipeBuilder.create(output) {
            cross8()
            define('A') { itemCreator.create(Items.EXPERIENCE_BOTTLE) }
            define('B') { itemCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD) }
            define('C') { itemCreator.create(Items.BOOK) }
            resultStack = HCItems.EXPERIENCE_TOME.toStack()
        }
        save(id(HTConst.SHAPELESS, "experience_tome"), HCExperienceStoringRecipe(CraftingBookCategory.MISC))

        // Eternal Upgrade
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "ABA",
                "ACA",
                "AAA",
            )
            define('A') { itemCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND) }
            define('B') { itemCreator.create(HCItems.ETERNAL_UPGRADE) }
            define('C') { itemCreator.create(HCItems.IRIDESCENT_POWDER) }
            resultStack = HCItems.ETERNAL_UPGRADE.toStack(2)
        }
        save(id(HTConst.SMITHING, "eternal_upgrade"), HCEternalSmithingRecipe)
        // Almighty Pickaxe
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(Items.NETHERITE_SHOVEL)
            ingredients += itemCreator.create(Items.NETHERITE_PICKAXE)
            ingredients += itemCreator.create(Items.NETHERITE_AXE)
            ingredients += itemCreator.create(Items.NETHERITE_HOE)
            repeat(4) {
                ingredients += itemCreator.create(CommonTagPrefixes.INGOT, CommonMaterialKeys.IRIDIUM)
            }
            resultStack = HCItems.ALMIGHTY_PICKAXE.toStack()
        }
    }

    @JvmStatic
    private fun buckets() {
        // Dye
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DyeContents) {
            HTShapelessRecipeBuilder.create(output) {
                repeat(4) {
                    ingredients += itemCreator.create(color.dyesTag)
                }
                ingredients += itemCreator.create(Tags.Items.BUCKETS_WATER)
                resultStack = content.bucketHolder.toStack()
                recipeId suffix "_from_bye"
            }
        }
    }

    @JvmStatic
    private fun copper() {
        // Tree Tap
        HTShapedRecipeBuilder.create(output) {
            pattern(
                " A ",
                "BBB",
                "B  ",
            )
            define('A') { itemCreator.create(Items.LEVER) }
            define('B') { itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER) }
            resultStack = HCBlocks.TREE_TAP.toStack()
        }
        // Copper Basin
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "A A",
                "A A",
                "BAB",
            )
            define('A') { itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER) }
            define('B') { itemCreator.create(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.COPPER) }
            resultStack = HCBlocks.COPPER_BASIN.unaffected.toStack()
        }
        for ((state: WeatheringCopper.WeatherState, base) in HCBlocks.COPPER_BASIN.weatheringMap) {
            val waxed: ItemLike = HCBlocks.COPPER_BASIN.waxedMap[state]!!
            // Waxing
            HTShapelessRecipeBuilder.create(output) {
                ingredients += itemCreator.create(base)
                ingredients += itemCreator.create(Items.HONEYCOMB)
                resultStack = ItemStack(waxed)
                recipeId suffix "_from_${base.path}"
            }
        }
    }

    @JvmStatic
    private fun getOrThrow(part: HTPartLike, material: HTMaterialLike): SupplierWithId<Item> = HiiragiCoreAccess.INSTANCE
        .registeredContents
        .items
        .getOrThrow(part, material)
}
