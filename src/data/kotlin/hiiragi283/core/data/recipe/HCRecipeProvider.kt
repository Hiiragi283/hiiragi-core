package hiiragi283.core.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.builder.saveSuffix
import hiiragi283.core.api.recipe.withSize
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.common.data.recipe.builder.HCChargingRecipeBuilder
import hiiragi283.core.common.tag.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

class HCRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(registries, output) {
    override fun buildRecipes() {
        // Warped Wart
        shapeless(RecipeCategory.FOOD, HCBlocks.WARPED_WART, 9)
            .requires(Items.WARPED_WART_BLOCK)
            .unlockedBy(getHasName(Items.WARPED_WART_BLOCK), has(Items.WARPED_WART_BLOCK))
            .save(output)

        // Almighty Pickaxe
        val almightyMaterial: TagKey<Item> = HiiragiCoreTags.Items.ALMIGHTY_PICKAXE_MATERIALS
        shapeless(RecipeCategory.TOOLS, HCItems.ALMIGHTY_PICKAXE)
            .requires(Items.NETHERITE_SHOVEL)
            .requires(Items.NETHERITE_PICKAXE)
            .requires(Items.NETHERITE_AXE)
            .requires(Items.NETHERITE_HOE)
            .requires(almightyMaterial)
            .requires(almightyMaterial)
            .requires(almightyMaterial)
            .requires(almightyMaterial)
            .unlockedBy("has_${almightyMaterial.location.path}", has(almightyMaterial))
            .save(output)

        buckets()
        charging()
    }

    private fun buckets() {
        // Dye
        for ((color: DyeColor, content: HTFluidContent) in HCFluids.DYE) {
            val dyeTag: TagKey<Item> = color.tag
            shapeless(RecipeCategory.MISC, content.getBucket())
                .requires(dyeTag)
                .requires(dyeTag)
                .requires(dyeTag)
                .requires(dyeTag)
                .requires(Tags.Items.BUCKETS_EMPTY)
                .unlockedBy("has_${color.serializedName}_dye", has(dyeTag))
                .save(output)
        }
        // Exp Bottle <-> Exp Bucket
        bottleToBucket(HCFluids.EXPERIENCE, Items.EXPERIENCE_BOTTLE)
        // Honey Bottle <-> Honey Bucket
        bottleToBucket(HCFluids.HONEY, Items.HONEY_BOTTLE)
        // Dragon Breath
        bottleToBucket(HCFluids.DRAGON_BREATH, Items.DRAGON_BREATH)

        // Mushroom Stew
        shapeless(RecipeCategory.MISC, HCFluids.MUSHROOM_STEW.getBucket())
            .requires(Items.MUSHROOM_STEW)
            .requires(Items.MUSHROOM_STEW)
            .requires(Items.MUSHROOM_STEW)
            .requires(Items.MUSHROOM_STEW)
            .requires(Tags.Items.BUCKETS_EMPTY)
            .unlockedBy("has_mushroom_stew_bucket", has(HCFluids.MUSHROOM_STEW.bucketTag))
            .saveSuffix(output, "_from_bowls")
    }

    private fun bottleToBucket(content: HTFluidContent, filled: ItemLike) {
        shapeless(RecipeCategory.MISC, content.getBucket())
            .requires(filled)
            .requires(filled)
            .requires(filled)
            .requires(filled)
            .requires(Tags.Items.BUCKETS_EMPTY)
            .unlockedBy("has_${content.path}_bucket", has(content.bucketTag))
            .saveSuffix(output, "_from_bottles")
        shapeless(RecipeCategory.MISC, filled, 4)
            .requires(content.bucketTag)
            .requires(Items.GLASS_BOTTLE)
            .requires(Items.GLASS_BOTTLE)
            .requires(Items.GLASS_BOTTLE)
            .requires(Items.GLASS_BOTTLE)
            .unlockedBy(getHasName(filled), has(filled))
            .saveSuffix(output, "_from_bucket")
    }

    private fun charging() {
        // Ender Pearl -> Ender Eye
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.ENDER_PEARLS) withSize 1
            result = resultCreator.create(Items.ENDER_EYE)
        }
        // Golden Apple
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.from(Items.GOLDEN_APPLE) withSize 1
            result = resultCreator.create(Items.ENCHANTED_GOLDEN_APPLE)
        }
        // Quartz -> Prismarine
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.GEMS_QUARTZ) withSize 1
            result = resultCreator.create(Items.PRISMARINE_SHARD)
        }
        // Redstone Dust -> Glowstone Dust
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.DUSTS_REDSTONE) withSize 1
            result = resultCreator.create(Items.GLOWSTONE_DUST)
        }
        // Honey Bottle -> Exp Bottle
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.DRINKS_HONEY) withSize 1
            result = resultCreator.create(Items.EXPERIENCE_BOTTLE)
        }

        // End Crystal -> Eldritch Pearl
        /*HCChargingRecipeBuilder.charging(output) {
            ingredient = itemCreator.from(Items.END_CRYSTAL) withSize 1
            result = resultCreator.material(CommonParts.PEARL, HCMaterialKeys.ELDRITCH)
        }
        // Heart of the Sea
        HCChargingRecipeBuilder.charging(output) {
            ingredient = itemCreator.from(HCItems.ELDER_HEART) withSize 1
            result = resultCreator.create(Items.HEART_OF_THE_SEA)
        }*/
        // Nether Star
        HCChargingRecipeBuilder.create(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.NETHER_STARS) withSize 1
            result = resultCreator.create(Items.NETHER_STAR)
        }
    }
}
