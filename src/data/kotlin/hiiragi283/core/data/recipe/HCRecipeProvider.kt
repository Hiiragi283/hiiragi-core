package hiiragi283.core.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.recipe.withSize
import hiiragi283.core.common.data.recipe.builder.HTItemToItemRecipeBuilder
import hiiragi283.core.common.tag.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
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

        charging()
    }

    private fun charging() {
        // Ender Pearl -> Ender Eye
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.ENDER_PEARLS) withSize 1
            result = resultCreator.create(Items.ENDER_EYE)
        }
        // Golden Apple
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = itemCreator.from(Items.GOLDEN_APPLE) withSize 1
            result = resultCreator.create(Items.ENCHANTED_GOLDEN_APPLE)
        }
        // Quartz -> Prismarine
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.GEMS_QUARTZ) withSize 1
            result = resultCreator.create(Items.PRISMARINE_SHARD)
        }
        // Redstone Dust -> Glowstone Dust
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.DUSTS_REDSTONE) withSize 1
            result = resultCreator.create(Items.GLOWSTONE_DUST)
        }
        // Honey Bottle -> Exp Bottle
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.DRINKS_HONEY) withSize 1
            result = resultCreator.create(Items.EXPERIENCE_BOTTLE)
        }

        // End Crystal -> Eldritch Pearl
        /*HTItemToItemRecipeBuilder.charging(output) {
            ingredient = itemCreator.from(Items.END_CRYSTAL) withSize 1
            result = resultCreator.material(CommonParts.PEARL, HCMaterialKeys.ELDRITCH)
        }
        // Heart of the Sea
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = itemCreator.from(HCItems.ELDER_HEART) withSize 1
            result = resultCreator.create(Items.HEART_OF_THE_SEA)
        }*/
        // Nether Star
        HTItemToItemRecipeBuilder.charging(output) {
            ingredient = itemCreator.fromTagKey(Tags.Items.NETHER_STARS) withSize 1
            result = resultCreator.create(Items.NETHER_STAR)
        }
    }
}
