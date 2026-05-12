package hiiragi283.core.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.block.HTWeatheringLevel
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidContent
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
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
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
                ingredients += Tags.Items.SANDS
            }
            ingredients += CommonTagPrefixes.DUST to CommonMaterialKeys.ASH
            resultStack += getOrThrow(CommonParts.DUST, VanillaMaterialKeys.GLASS) to 4
            recipeId suffix "_from_sand_and_ash"
        }
        // Glass Dust -> Glass
        HTCookingRecipeBuilder.smelting(output) {
            ingredient = itemCreator.create(getOrThrow(CommonParts.DUST, VanillaMaterialKeys.GLASS))
            resultStack += Items.GLASS
            recipeId suffix "_from_dust"
        }
        // Iron Rod -> Iron Bar
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "AAA",
            )
            define('A') += CommonTagPrefixes.ROD to VanillaMaterialKeys.IRON
            resultStack += Items.IRON_BARS to 8
            recipeId suffix "_from_rod"
        }
        // Sticky Piston
        HTShapelessRecipeBuilder.create(output) {
            ingredients += HiiragiCoreTags.Items.STICKY_BALLS
            ingredients += Items.PISTON
            resultStack += Items.STICKY_PISTON
        }

        // Steel + Flint
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.INGOT to CommonMaterialKeys.STEEL
            ingredients += Items.FLINT
            resultStack += createItemStack(Items.FLINT_AND_STEEL, DataComponents.MAX_DAMAGE, 64 * 3)
            recipeId replace id("real_flint_and_steel")
        }
    }

    @JvmStatic
    private fun materials() {
        // Bamboo -> Bamboo Charcoal
        HTCookingRecipeBuilder.smelting(output) {
            ingredient = itemCreator.create(Items.BAMBOO)
            resultStack += HCItems.BAMBOO_CHARCOAL
            exp = 0.5f
        }
        // Compressed Sawdust -> Particle Board
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.DUST to VanillaMaterialKeys.WOOD
            define('B') += HiiragiCoreTags.Items.STICKY_BALLS
            resultStack += HCItems.PARTICLE_BOARD to 4
        }

        // Steel Compound
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.INGOT to VanillaMaterialKeys.IRON
            repeat(2) {
                ingredients += CommonTagPrefixes.DUST to VanillaMaterialKeys.CHARCOAL
            }
            resultStack += HCItems.STEEL_COMPOUND
            recipeId suffix "_with_charcoal"
        }
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.INGOT to VanillaMaterialKeys.IRON
            repeat(4) {
                ingredients += CommonTagPrefixes.DUST to VanillaMaterialKeys.COAL
            }
            resultStack += HCItems.STEEL_COMPOUND
            recipeId suffix "_with_coal"
        }
        HTCookingRecipeBuilder.blasting(output) {
            ingredient = itemCreator.create(HCItems.STEEL_COMPOUND)
            resultStack += getOrThrow(CommonParts.INGOT, CommonMaterialKeys.STEEL)
            exp = 0.7f
            recipeId suffix "_from_compound"
        }

        // Polymer Resin -> Plastic Bar
        HTCookingRecipeBuilder.smelting(output) {
            ingredient = itemCreator.create(HCItems.POLYMER_RESIN)
            resultStack += getOrThrow(CommonParts.PLATE, CommonMaterialKeys.PLASTIC)
            exp = 0.7f
            recipeId suffix "_from_resin"
        }
        // Synthetic
        for (item: ItemLike in listOf(HCItems.SYNTHETIC_FEATHER, HCItems.SYNTHETIC_FIBER, HCItems.SYNTHETIC_LEATHER)) {
            HTStonecuttingRecipeBuilder.create(output) {
                ingredient = itemCreator.create(HiiragiCoreTags.Items.PLASTICS)
                resultStack += item
            }
        }

        // Warped Wart Block
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += HiiragiCoreTags.Items.CROPS_WARPED_WART
            define('B') += HCBlocks.WARPED_WART
            resultStack += Items.WARPED_WART_BLOCK
        }
        // Flour + Water -> Dough
        HTShapelessRecipeBuilder.create(output) {
            ingredients += HiiragiCoreTags.Items.FLOURS_WHEAT
            ingredients += DataComponentIngredient.of(
                false,
                DataComponents.POTION_CONTENTS,
                PotionContents(Potions.WATER),
                Items.POTION,
            )
            resultStack += HCItems.WHEAT_DOUGH
            recipeId suffix "_with_bottle"
        }
        HTShapelessRecipeBuilder.create(output) {
            repeat(3) {
                ingredients += HiiragiCoreTags.Items.FLOURS_WHEAT
            }
            ingredients += Tags.Items.BUCKETS_WATER
            resultStack += HCItems.WHEAT_DOUGH to 3
            recipeId suffix "_with_bucket"
        }
        // Dough -> Bread
        HTCookingRecipeBuilder.smeltingAndSmoking(output) {
            ingredient = itemCreator.create(HCItems.WHEAT_DOUGH)
            resultStack += Items.BREAD
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
            define('A') += Items.WITHER_SKELETON_SKULL
            define('B') += ItemTags.SOUL_FIRE_BASE_BLOCKS
            resultStack += HCItems.WITHER_DOLL
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
        // Bucket
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "A A",
                " A ",
            )
            define('A') += entry
            resultStack += Items.BUCKET to multiplier.toInt()
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
            define('A') += entry
            define('B') += Tags.Items.CHESTS
            resultStack += Items.HOPPER to multiplier.toInt()
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
            define('A') += ItemTags.PLANKS
            define('B') += ItemTags.STONE_CRAFTING_MATERIALS
            define('C') += entry
            define('D') += CommonTagPrefixes.DUST to VanillaMaterialKeys.REDSTONE
            resultStack += Items.PISTON to multiplier.toInt()
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
            define('A') += entry
            define('B') += Tags.Items.RODS_WOODEN
            resultStack += Items.RAIL to (16 * multiplier).toInt()
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
            define('A') += ItemTags.WOOL
            define('B') += CommonTagPrefixes.INGOT to VanillaMaterialKeys.COPPER
            define('C') += Tags.Items.RODS_WOODEN
            resultStack += HCItems.PAINT_BRUSH
        }

        // Blueprint
        HTShapelessRecipeBuilder.create(output) {
            ingredients += Items.PAPER
            ingredients += Tags.Items.DYES_WHITE
            ingredients += Tags.Items.DYES_BLUE
            resultStack += HCItems.BLUEPRINT
        }
        save(id(HTConst.SHAPELESS, "blueprint_cloning"), HTBlueprintCloningRecipe(CraftingBookCategory.MISC))
        // Bomb
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "  A",
                "BC ",
                "CB ",
            )
            define('A') += Tags.Items.STRINGS
            define('B') += CommonTagPrefixes.NUGGET to VanillaMaterialKeys.IRON
            define('C') += Tags.Items.GUNPOWDERS
            resultStack += HCItems.BOMB
        }
        // Slot Cover
        HTStonecuttingRecipeBuilder.create(output) {
            ingredient = itemCreator.create(Items.SMOOTH_STONE_SLAB)
            resultStack += HCItems.SLOT_COVER to 3
        }
        // Trader Catalog
        HTShapelessRecipeBuilder.create(output) {
            ingredients += Items.BOOK
            ingredients += CommonTagPrefixes.GEM to VanillaMaterialKeys.EMERALD
            resultStack += HCItems.TRADER_CATALOG
        }
        // Eldritch Egg
        HTShapedRecipeBuilder.create(output) {
            hollow4()
            define('A') += Tags.Items.EGGS
            define('B') += CommonTagPrefixes.PEARL to HCMaterialKeys.ELDRITCH
            resultStack += HCItems.ELDRITCH_EGG to 4
        }
        // Experience Tome
        HTShapedRecipeBuilder.create(output) {
            cross8()
            define('A') += Items.EXPERIENCE_BOTTLE
            define('B') += CommonTagPrefixes.GEM to VanillaMaterialKeys.EMERALD
            define('C') += Items.BOOK
            resultStack += HCItems.EXPERIENCE_TOME
        }
        save(id(HTConst.SHAPELESS, "experience_tome"), HCExperienceStoringRecipe(CraftingBookCategory.MISC))

        // Eternal Upgrade
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "ABA",
                "ACA",
                "AAA",
            )
            define('A') += CommonTagPrefixes.GEM to VanillaMaterialKeys.DIAMOND
            define('B') += HCItems.ETERNAL_UPGRADE
            define('C') += HCItems.IRIDESCENT_POWDER
            resultStack += HCItems.ETERNAL_UPGRADE to 2
        }
        save(id(HTConst.SMITHING, "eternal_upgrade"), HCEternalSmithingRecipe)
        // Almighty Pickaxe
        HTShapelessRecipeBuilder.create(output) {
            ingredients += Items.NETHERITE_SHOVEL
            ingredients += Items.NETHERITE_PICKAXE
            ingredients += Items.NETHERITE_AXE
            ingredients += Items.NETHERITE_HOE
            repeat(4) {
                ingredients += CommonTagPrefixes.INGOT to CommonMaterialKeys.IRIDIUM
            }
            resultStack += HCItems.ALMIGHTY_PICKAXE
        }
    }

    @JvmStatic
    private fun buckets() {
        // Dye
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DyeContents) {
            HTShapelessRecipeBuilder.create(output) {
                repeat(4) {
                    ingredients += color.dyesTag
                }
                ingredients += Tags.Items.BUCKETS_WATER
                resultStack += content.getBucket()
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
            define('A') += Items.LEVER
            define('B') += CommonTagPrefixes.INGOT to VanillaMaterialKeys.COPPER
            resultStack += HCBlocks.TREE_TAP
        }
        // Copper Basin
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "A A",
                "A A",
                "BAB",
            )
            define('A') += CommonTagPrefixes.INGOT to VanillaMaterialKeys.COPPER
            define('B') += CommonTagPrefixes.STORAGE_BLOCK to VanillaMaterialKeys.COPPER
            resultStack += HCBlocks.COPPER_BASINS[HTWeatheringLevel.UNAFFECTED]?.first?.get() ?: return
        }
        HCBlocks.COPPER_BASINS.forEach { base: HTBlockHolderLike<*>, waxed: HTBlockHolderLike<*> ->
            // Waxing
            HTShapelessRecipeBuilder.create(output) {
                ingredients += base.get()
                ingredients += Items.HONEYCOMB
                resultStack += waxed.get()
                recipeId suffix "_from_${base.path}"
            }
        }
    }

    @JvmStatic
    private fun getOrThrow(part: HTPartLike, material: HTMaterialLike): Item = HiiragiCoreAccess.INSTANCE
        .registeredContents
        .items
        .getOrThrow(part, material)
        .get()
}
