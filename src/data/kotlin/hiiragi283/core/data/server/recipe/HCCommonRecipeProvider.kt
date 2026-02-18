package hiiragi283.core.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.crafting.HTEternalSmithingRecipe
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
        HTShapelessRecipeBuilder.create(output) {
            repeat(3) {
                ingredients += Tags.Items.SANDS
            }
            ingredients += CommonTagPrefixes.DUST to CommonMaterialKeys.ASH
            resultStack += getOrThrow(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLASS) to 4
            recipeId suffix "_from_sand_and_ash"
        }
        // Glass Dust -> Glass
        HTCookingRecipeBuilder.smelting(output) {
            ingredient += getOrThrow(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLASS)
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
        // Reinforced Deepslate
        /*HTShapedRecipeBuilder.create(output) {
            hollow4()
            define('A') += CommonTagPrefixes.PLATE to HCMaterialKeys.ANCIENT_METAL
            define('B') += Items.DEEPSLATE
            resultStack += Items.REINFORCED_DEEPSLATE
        }*/

        // Warped Wart Block
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += HiiragiCoreTags.Items.CROPS_WARPED_WART
            define('B') += HCBlocks.WARPED_WART
            resultStack += Items.WARPED_WART_BLOCK
        }

        // Compressed Sawdust -> Charcoal
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.DUST to VanillaMaterialKeys.WOOD
            define('B') += CommonToolTypes.HAMMER
            resultStack += HCItems.COMPRESSED_SAWDUST
        }
        HTCookingRecipeBuilder.smelting(output) {
            ingredient += HCItems.COMPRESSED_SAWDUST
            resultStack += Items.CHARCOAL
            exp = 0.5f
            time = 20 * 30
            recipeId suffix "_from_sawdust"
        }
        // Compressed Sawdust -> Particle Board
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.DUST to VanillaMaterialKeys.WOOD
            define('B') += Tags.Items.SLIME_BALLS
            resultStack += HCItems.PARTICLE_BOARD to 4
        }
        // Dough -> Bread
        HTCookingRecipeBuilder.smeltingAndSmoking(output) {
            ingredient += getOrThrow(CommonTagPrefixes.DOUGH, VanillaMaterialKeys.WHEAT)
            resultStack += Items.BREAD
            exp = 0.3f
            recipeId suffix "_from_dough"
        }

        // Bamboo -> Bamboo Charcoal
        HTCookingRecipeBuilder.smelting(output) {
            ingredient += Items.BAMBOO
            resultStack += HCItems.BAMBOO_CHARCOAL
            exp = 0.5f
        }
        // Polymer Resin -> Plastic Bar
        HTCookingRecipeBuilder.smelting(output) {
            ingredient += HCItems.POLYMER_RESIN
            resultStack += getOrThrow(CommonTagPrefixes.PLATE, CommonMaterialKeys.PLASTIC)
            exp = 0.7f
            recipeId suffix "_from_resin"
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
            ingredient += HCItems.STEEL_COMPOUND
            resultStack += getOrThrow(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL)
            exp = 0.7f
            recipeId suffix "_from_compound"
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
    }

    @JvmStatic
    private fun utilities() {
        // Ancient Upgrade
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "ABA",
                "ACA",
                "AAA",
            )
            define('A') += CommonTagPrefixes.GEM to VanillaMaterialKeys.DIAMOND
            define('B') += HCItems.ANCIENT_UPGRADE
            define('C') += Items.DEEPSLATE
            resultStack += HCItems.ANCIENT_UPGRADE
        }

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
            ingredient += Items.SMOOTH_STONE_SLAB
            resultStack += HCItems.SLOT_COVER to 3
        }
        // Trader Catalog
        HTShapelessRecipeBuilder.create(output) {
            ingredients += Items.BOOK
            ingredients += CommonTagPrefixes.GEM to VanillaMaterialKeys.EMERALD
            resultStack += HCItems.TRADER_CATALOG
            category = CraftingBookCategory.EQUIPMENT
        }
        // Eldritch Egg
        HTShapedRecipeBuilder.create(output) {
            hollow4()
            define('A') += Tags.Items.EGGS
            define('B') += CommonTagPrefixes.PEARL to HCMaterialKeys.ELDRITCH
            resultStack += HCItems.ELDRITCH_EGG to 4
            category = CraftingBookCategory.EQUIPMENT
        }

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
            resultStack += HCItems.ETERNAL_UPGRADE
        }
        save(id(HTConst.SMITHING, "eternal_upgrade"), HTEternalSmithingRecipe)
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
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DYE) {
            HTShapelessRecipeBuilder.create(output) {
                repeat(4) {
                    ingredients += color.dyesTag
                }
                ingredients += Tags.Items.BUCKETS_WATER
                resultStack += content.getBucket()
                recipeId suffix "_from_bye"
            }
        }

        // Exp Bottle <-> Exp Bucket
        HTShapelessRecipeBuilder.create(output) {
            repeat(4) {
                ingredients += Items.EXPERIENCE_BOTTLE
            }
            ingredients += Tags.Items.BUCKETS_EMPTY
            resultStack += HCFluids.EXPERIENCE.getBucket()
            recipeId suffix "_from_bottles"
        }
        HTShapelessRecipeBuilder.create(output) {
            ingredients += HCFluids.EXPERIENCE.bucketTag
            repeat(4) {
                ingredients += Items.GLASS_BOTTLE
            }
            resultStack += Items.EXPERIENCE_BOTTLE to 4
            recipeId suffix "_from_bucket"
        }

        // Honey Bottle <-> Honey Bucket
        HTShapelessRecipeBuilder.create(output) {
            repeat(4) {
                ingredients += Tags.Items.DRINKS_HONEY
            }
            ingredients += Tags.Items.BUCKETS_EMPTY
            resultStack += HCFluids.HONEY.getBucket()
            recipeId suffix "_from_bottles"
        }
        HTShapelessRecipeBuilder.create(output) {
            ingredients += HCFluids.HONEY.bucketTag
            repeat(4) {
                ingredients += Items.GLASS_BOTTLE
            }
            resultStack += Items.HONEY_BOTTLE to 4
            recipeId suffix "_from_bucket"
        }
        // Honey Block <-> Honey Bucket
        HTShapelessRecipeBuilder.create(output) {
            ingredients += Items.HONEY_BLOCK
            ingredients += Tags.Items.BUCKETS_EMPTY
            resultStack += HCFluids.HONEY.getBucket()
            recipeId suffix "_from_block"
        }
        HTShapelessRecipeBuilder.create(output) {
            ingredients += HCFluids.HONEY.bucketTag
            resultStack += Items.HONEY_BLOCK
            recipeId suffix "_from_bucket"
        }

        // Mushroom Stew
        HTShapelessRecipeBuilder.create(output) {
            repeat(4) {
                ingredients += Items.MUSHROOM_STEW
            }
            ingredients += Tags.Items.BUCKETS_EMPTY
            resultStack += HCFluids.MUSHROOM_STEW.getBucket()
            recipeId suffix "_from_bowls"
        }

        // Dragon Breath
        HTShapelessRecipeBuilder.create(output) {
            repeat(4) {
                ingredients += Items.DRAGON_BREATH
            }
            ingredients += Tags.Items.BUCKETS_EMPTY
            resultStack += HCFluids.DRAGON_BREATH.getBucket()
            recipeId suffix "_from_bottles"
        }
        HTShapelessRecipeBuilder.create(output) {
            ingredients += HCFluids.DRAGON_BREATH.bucketTag
            repeat(4) {
                ingredients += Items.GLASS_BOTTLE
            }
            resultStack += Items.DRAGON_BREATH to 4
            recipeId suffix "_from_bucket"
        }

        // Latex -> Raw Rubber
        HTShapelessRecipeBuilder.create(output) {
            ingredients += HCFluids.LATEX.bucketTag
            resultStack += HCItems.RAW_RUBBER
            recipeId suffix "_from_bucket"
        }
        // Raw Rubber -> Rubber Bar
        HTCookingRecipeBuilder.smelting(output) {
            ingredient += HCItems.RAW_RUBBER
            resultStack += getOrThrow(CommonTagPrefixes.INGOT, CommonMaterialKeys.RUBBER)
            exp = 0.7f
            recipeId suffix "_from_raw"
        }
    }

    @JvmStatic
    private fun getOrThrow(prefix: HTTagPrefix, material: HTMaterialLike): HTItemHolderLike<*> = HiiragiCoreAccess.INSTANCE
        .registeredContents
        .items
        .getOrThrow(prefix, material)
}
