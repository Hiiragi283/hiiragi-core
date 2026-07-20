package hiiragi283.core.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.copper.HTCopperPhase
import hiiragi283.core.api.data.ConditionBuilder
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.registry.HTDeferredBlockAndItem
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.times
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.common.crafting.HCExperienceStoringRecipe
import hiiragi283.core.common.crafting.HTBlueprintCloningRecipe
import hiiragi283.core.common.data.recipe.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.HTStonecuttingRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import org.apache.commons.lang3.math.Fraction

class HCVanillaRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipes() {
        vanilla()
        materials()
        utilities()
        buckets()

        copper()
    }

    private fun vanilla() {
        // Sand + Ash -> Glass Dust
        useItem(CommonParts.DUST, VanillaMaterialKeys.GLASS) {
            HTShapelessRecipeBuilder.create {
                repeat(3) { ingredient { +Tags.Items.SANDS } }
                ingredient { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.ASH) }
                +it.toStack(4)
                recipeId suffix "_from_sand_and_ash"
            }.save(exporter)
        }
        // Glass Dust -> Glass
        useItem(CommonParts.DUST, VanillaMaterialKeys.GLASS) {
            HTCookingRecipeBuilder.smelting {
                ingredient { +it }
                result { +Items.GLASS }
                recipeId suffix "_from_dust"
            }.save(exporter)
        }
        // Iron Rod -> Iron Bar
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"AAA"
            define('A') { +tag(CommonTagPrefixes.ROD, VanillaMaterialKeys.IRON) }
            result {
                +Items.IRON_BARS
                count = 8
            }
            recipeId suffix "_from_rod"
        }.save(exporter)
        // Sticky Piston
        HTShapelessRecipeBuilder.create {
            ingredient { +HiiragiCoreTags.Items.STICKY_BALLS }
            ingredient { +Items.PISTON }
            result { +Items.STICKY_PISTON }
        }.save(exporter)

        // Steel + Flint
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL) }
            ingredient { +Items.FLINT }
            +createItemStack(Items.FLINT_AND_STEEL, DataComponents.MAX_DAMAGE, 64 * 3)
            recipeId replace id("real_flint_and_steel")
        }.save(exporter)
    }

    private fun materials() {
        // Bamboo -> Bamboo Charcoal
        HTCookingRecipeBuilder.smelting {
            ingredient { +Items.BAMBOO }
            +HCItems.BAMBOO_CHARCOAL.toStack()
            exp = 0.5f
        }.save(exporter)
        // Compressed Sawdust -> Particle Board
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD) }
            define('B') { +HiiragiCoreTags.Items.STICKY_BALLS }
            +HCItems.PARTICLE_BOARD.toStack(4)
        }.save(exporter)

        // Steel Compound
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON) }
            repeat(2) {
                ingredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.CHARCOAL) }
            }
            +HCItems.STEEL_COMPOUND.toStack()
            recipeId suffix "_with_charcoal"
        }.save(exporter)
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON) }
            repeat(4) {
                ingredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL) }
            }
            +HCItems.STEEL_COMPOUND.toStack()
            recipeId suffix "_with_coal"
        }.save(exporter)
        useItem(CommonParts.INGOT, CommonMaterialKeys.STEEL) {
            HTCookingRecipeBuilder.blasting {
                ingredient { +HCItems.STEEL_COMPOUND }
                +it.toStack()
                exp = 0.7f
                recipeId suffix "_from_compound"
            }.save(exporter)
        }

        // Polymer Resin -> Plastic Bar
        useItem(CommonParts.PLATE, CommonMaterialKeys.PLASTIC) {
            HTCookingRecipeBuilder.smelting {
                ingredient { +HCItems.POLYMER_RESIN }
                +it.toStack()
                exp = 0.7f
                recipeId suffix "_from_resin"
            }.save(exporter)
        }
        // Synthetic
        for (item: HTSimpleDeferredItem in listOf(HCItems.SYNTHETIC_FEATHER, HCItems.SYNTHETIC_FIBER, HCItems.SYNTHETIC_LEATHER)) {
            HTStonecuttingRecipeBuilder.create {
                ingredient { +HiiragiCoreTags.Items.PLASTICS }
                +item.toStack()
            }.save(exporter)
        }

        // Warped Wart Block
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +HiiragiCoreTags.Items.CROPS_WARPED_WART }
            define('B') { +HCBlocks.WARPED_WART }
            result { +Items.WARPED_WART_BLOCK }
        }.save(exporter)
        // Flour + Water -> Dough
        HTShapelessRecipeBuilder.create {
            ingredient { +HiiragiCoreTags.Items.FLOURS_WHEAT }
            +DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, PotionContents(Potions.WATER), Items.POTION)
            +HCItems.WHEAT_DOUGH.toStack()
            recipeId suffix "_with_bottle"
        }.save(exporter)
        HTShapelessRecipeBuilder.create {
            repeat(3) {
                ingredient { +HiiragiCoreTags.Items.FLOURS_WHEAT }
            }
            ingredient { +Tags.Items.BUCKETS_WATER }
            +HCItems.WHEAT_DOUGH.toStack(3)
            recipeId suffix "_with_bucket"
        }.save(exporter)
        // Dough -> Bread
        HTCookingRecipeBuilder.smeltingAndSmoking {
            ingredient { +HCItems.WHEAT_DOUGH }
            result { +Items.BREAD }
            exp = 0.3f
            recipeId suffix "_from_dough"
        }.forEach { it.save(exporter) }

        // Wither Doll
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"BBB"
            +" B "
            define('A') { +Items.WITHER_SKELETON_SKULL }
            define('B') { +ItemTags.SOUL_FIRE_BASE_BLOCKS }
            +HCItems.WITHER_DOLL.toStack()
        }.save(exporter)

        registerIronAlt(CommonMaterialKeys.BRONZE, fraction(3, 2))
        registerIronAlt(CommonMaterialKeys.BRASS, fraction(3, 2))
        registerIronAlt(CommonMaterialKeys.STEEL, fraction(2))
        registerIronAlt(CommonMaterialKeys.INVAR, fraction(2))
    }

    private fun registerIronAlt(key: HTMaterialKey, multiplier: Fraction) {
        val suffix: String = key.path
        val ingredient: IngredientBuilder.() -> Unit = { +tag(CommonTagPrefixes.INGOT, key) }
        val condition: ConditionBuilder.() -> Unit = { +CommonTagPrefixes.INGOT.itemTagKey(key) }
        // Bucket
        HTShapedRecipeBuilder.create {
            +"A A"
            +" A "
            define('A', ingredient)
            result {
                +Items.BUCKET
                count = multiplier.toInt()
            }
            recipeId suffix "_from_$suffix"
            condition(condition)
        }.save(exporter)
        // Hopper
        HTShapedRecipeBuilder.create {
            +"A A"
            +"ABA"
            +" A "
            define('A', ingredient)
            define('B') { +Tags.Items.CHESTS }
            result {
                +Items.HOPPER
                count = multiplier.toInt()
            }
            recipeId suffix "_from_$suffix"
            condition(condition)
        }.save(exporter)
        // Piston
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"BCB"
            +"BDB"
            define('A') { +ItemTags.PLANKS }
            define('B') { +ItemTags.STONE_CRAFTING_MATERIALS }
            define('C', ingredient)
            define('D') { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE) }
            result {
                +Items.PISTON
                count = multiplier.toInt()
            }
            recipeId suffix "_from_$suffix"
            condition(condition)
        }.save(exporter)
        // Rail
        HTShapedRecipeBuilder.create {
            +"A A"
            +"ABA"
            +"A A"
            define('A', ingredient)
            define('B') { +Tags.Items.RODS_WOODEN }
            result {
                +Items.RAIL
                count = (16 * multiplier).toInt()
            }
            recipeId suffix "_from_$suffix"
            condition(condition)
        }.save(exporter)
    }

    private fun utilities() {
        // Paint Brush
        HTShapedRecipeBuilder.create {
            +"A"
            +"B"
            +"C"
            define('A') { +ItemTags.WOOL }
            define('B') { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER) }
            define('C') { +Tags.Items.RODS_WOODEN }
            +HCItems.PAINT_BRUSH.toStack()
        }.save(exporter)
        // Blueprint
        HTShapelessRecipeBuilder.create {
            ingredient { +Items.PAPER }
            ingredient { +Tags.Items.DYES_WHITE }
            ingredient { +Tags.Items.DYES_BLUE }
            +HCItems.BLUEPRINT.toStack()
        }.save(exporter)
        exporter.accept(id(HTConst.SHAPELESS, "blueprint_cloning"), HTBlueprintCloningRecipe(CraftingBookCategory.MISC))
        // Bomb
        HTShapedRecipeBuilder.create {
            +"  A"
            +"BC "
            +"CB "
            define('A') { +Tags.Items.STRINGS }
            define('B') { +tag(CommonTagPrefixes.NUGGET, VanillaMaterialKeys.IRON) }
            define('C') { +Tags.Items.GUNPOWDERS }
            +HCItems.BOMB.toStack()
        }.save(exporter)
        // Slot Cover
        HTStonecuttingRecipeBuilder.create {
            ingredient { +Items.SMOOTH_STONE_SLAB }
            +HCItems.SLOT_COVER.toStack(3)
        }.save(exporter)
        // Trader Catalog
        HTShapelessRecipeBuilder.create {
            ingredient { +Items.BOOK }
            ingredient { +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD) }
            +HCItems.TRADER_CATALOG.toStack()
        }.save(exporter)
        // Eldritch Egg
        HTShapedRecipeBuilder.create {
            hollow4()
            define('A') { +Tags.Items.EGGS }
            define('B') { +tag(CommonTagPrefixes.PEARL, HCMaterialKeys.ELDRITCH) }
            +HCItems.ELDRITCH_EGG.toStack(4)
        }.save(exporter)
        // Experience Tome
        HTShapedRecipeBuilder.create {
            cross8()
            define('A') { +Items.EXPERIENCE_BOTTLE }
            define('B') { +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD) }
            define('C') { +Items.BOOK }
            +HCItems.EXPERIENCE_TOME.toStack()
        }.save(exporter)
        exporter.accept(id(HTConst.SHAPELESS, "experience_tome"), HCExperienceStoringRecipe(CraftingBookCategory.MISC))

        // Almighty Pickaxe
        HTShapelessRecipeBuilder.create {
            ingredient { +Items.NETHERITE_SHOVEL }
            ingredient { +Items.NETHERITE_PICKAXE }
            ingredient { +Items.NETHERITE_AXE }
            ingredient { +Items.NETHERITE_HOE }
            repeat(4) { ingredient { +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.IRIDIUM) } }
            +HCItems.ALMIGHTY_PICKAXE.toStack()
        }.save(exporter)
        // Eternal Upgrade
        HTShapedRecipeBuilder.create {
            +"ABA"
            +"ACA"
            +"AAA"
            define('A') { +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND) }
            define('B') { +HCItems.ETERNAL_UPGRADE }
            define('C') { +HCItems.IRIDESCENT_POWDER }
            +HCItems.ETERNAL_UPGRADE.toStack(2)
        }.save(exporter)
        exporter.accept(id(HTConst.SMITHING, "eternal_upgrade"), HCEternalSmithingRecipe)
        // Ring of Hyperion
        HTShapedRecipeBuilder.create {
            cross8()
            define('A') { +Items.WIND_CHARGE }
            define('B') { +Items.ELYTRA }
            define('C') { +HCItems.IRIDESCENT_POWDER }
            +HCItems.RING_OF_HYPERION.toStack()
        }.save(exporter)
    }

    private fun buckets() {
        // Dye
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DYES.asSequenceWithColor()) {
            HTShapelessRecipeBuilder.create {
                repeat(4) { ingredient { +color.dyesTag } }
                ingredient { +Tags.Items.BUCKETS_WATER }
                +content.bucketHolder.toStack()
                recipeId suffix "_from_bye"
            }.save(exporter)
        }
    }

    private fun copper() {
        // Tree Tap
        HTShapedRecipeBuilder.create {
            +" A "
            +"BBB"
            +"B  "
            define('A') { +Items.LEVER }
            define('B') { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER) }
            +HCBlocks.TREE_TAP.toStack()
        }.save(exporter)
        // Copper Basin
        HTShapedRecipeBuilder.create {
            +"A A"
            +"A A"
            +"BAB"
            define('A') { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER) }
            define('B') { +tag(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.COPPER) }
            result { +HCBlocks.COPPER_BASIN.weathering.unaffected }
        }.save(exporter)

        for (phase: HTCopperPhase in HTCopperPhase.entries) {
            val (base: HTDeferredBlockAndItem<*, *>, waxed: HTDeferredBlockAndItem<*, *>) = HCBlocks.COPPER_BASIN[phase]
            // Waxing
            HTShapelessRecipeBuilder.create {
                ingredient { +base }
                ingredient { +Items.HONEYCOMB }
                result { +waxed }
                group = "copper_basin"
                recipeId replace waxed.getId().withSuffix("_from_honeycomb")
            }.save(exporter)
        }
    }

    override fun getName(): String = "Common Recipes"
}
