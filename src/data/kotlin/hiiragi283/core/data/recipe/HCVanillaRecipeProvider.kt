package hiiragi283.core.data.recipe

import hiiragi283.core.api.HTDyeColor
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.VanillaMaterials
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

class HCVanillaRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(registries, output) {
    override fun buildRecipes() {
        // Warped Wart
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.fromItem(Items.WARPED_WART_BLOCK)
            result += HCBlocks.WARPED_WART to 9
        }
        // Crucible
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "A A",
                "A A",
                "BCB",
            )
            define('A')(itemCreator.fromTagKey(Tags.Items.BRICKS_NORMAL))
            define('B')(itemCreator.from(Items.BRICKS))
            define('C')(itemCreator.fromMaterial(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterials.COPPER))
            result += HCBlocks.CRUCIBLE
        }
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "A A",
                "A A",
                "BCB",
            )
            define('A')(itemCreator.fromTagKey(Tags.Items.BRICKS_NETHER))
            define('B')(itemCreator.from(Items.NETHER_BRICKS))
            define('C')(itemCreator.fromMaterial(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterials.GOLD))
            result += HCBlocks.NETHER_CRUCIBLE
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
}
