package hiiragi283.core.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.common.tag.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

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
    }
}
