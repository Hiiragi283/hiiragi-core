package hiiragi283.core.data.recipe

import hiiragi283.core.api.HTDyeColor
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.common.data.recipe.builder.HCChargingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.tag.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

class HCRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(registries, output) {
    override fun buildRecipes() {
        // Warped Wart
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.fromItem(Items.WARPED_WART_BLOCK)
            result += HCBlocks.WARPED_WART to 9
        }

        // Almighty Pickaxe
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.fromItem(Items.NETHERITE_SHOVEL)
            ingredients += itemCreator.fromItem(Items.NETHERITE_PICKAXE)
            ingredients += itemCreator.fromItem(Items.NETHERITE_AXE)
            ingredients += itemCreator.fromItem(Items.NETHERITE_HOE)
            repeat(4) {
                ingredients += itemCreator.fromTagKey(HiiragiCoreTags.Items.ALMIGHTY_PICKAXE_MATERIALS)
            }
            result += HCItems.ALMIGHTY_PICKAXE
            category = RecipeCategory.TOOLS
        }

        buckets()
        charging()
    }

    private fun buckets() {
        // Dye
        for ((color: HTDyeColor, content: HTFluidContent) in HCFluids.DYE) {
            HTShapelessRecipeBuilder.create(output) {
                repeat(4) {
                    ingredients += itemCreator.fromTagKey(color.dyesTag)
                }
                ingredients += itemCreator.fromTagKey(Tags.Items.BUCKETS_EMPTY)
                result += content.getBucket()
            }
        }
        // Exp Bottle <-> Exp Bucket
        bottleToBucket(HCFluids.EXPERIENCE, Items.EXPERIENCE_BOTTLE)
        // Honey Bottle <-> Honey Bucket
        bottleToBucket(HCFluids.HONEY, Items.HONEY_BOTTLE)
        // Dragon Breath
        bottleToBucket(HCFluids.DRAGON_BREATH, Items.DRAGON_BREATH)

        // Mushroom Stew
        HTShapelessRecipeBuilder.create(output) {
            repeat(4) {
                ingredients += itemCreator.fromItem(Items.MUSHROOM_STEW)
            }
            ingredients += itemCreator.fromTagKey(Tags.Items.BUCKETS_EMPTY)
            result += HCFluids.MUSHROOM_STEW.getBucket()
            recipeId suffix "_from_bowls"
        }
    }

    private fun bottleToBucket(content: HTFluidContent, filled: ItemLike) {
        HTShapelessRecipeBuilder.create(output) {
            repeat(4) {
                ingredients += itemCreator.fromItem(filled)
            }
            ingredients += itemCreator.fromTagKey(Tags.Items.BUCKETS_EMPTY)
            result += content.getBucket()
            recipeId suffix "_from_bottles"
        }
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.fromTagKey(content.bucketTag)
            repeat(4) {
                ingredients += itemCreator.fromItem(Items.GLASS_BOTTLE)
            }
            result += filled to 4
            recipeId suffix "_from_bucket"
        }
    }

    private fun charging() {
        // Ender Pearl -> Ender Eye
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.ENDER_PEARLS)
            result += Items.ENDER_EYE
        }
        // Golden Apple
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.from(Items.GOLDEN_APPLE)
            result += Items.ENCHANTED_GOLDEN_APPLE
        }
        // Quartz -> Prismarine
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.GEMS_QUARTZ)
            result += Items.PRISMARINE_SHARD
        }
        // Redstone Dust -> Glowstone Dust
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.DUSTS_REDSTONE)
            result += Items.GLOWSTONE_DUST
        }
        // Honey Bottle -> Exp Bottle
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.DRINKS_HONEY)
            result += Items.EXPERIENCE_BOTTLE
        }

        // End Crystal -> Eldritch Pearl
        /*HCChargingRecipeBuilder.charging(output) {
            ingredient = itemCreator.from(Items.END_CRYSTAL)
            result += CommonParts.PEARL, HCMaterialKeys.ELDRITCH
        }
        // Heart of the Sea
        HCChargingRecipeBuilder.charging(output) {
            ingredient = itemCreator.from(HCItems.ELDER_HEART)
            result += Items.HEART_OF_THE_SEA
        }*/
        // Nether Star
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.NETHER_STARS)
            result += Items.NETHER_STAR
        }
    }
}
