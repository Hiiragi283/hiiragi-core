package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.core.setup.HCMaterialContents
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.math.fraction
import hiiragi283.lib.tag.CommonTagPrefixes
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class HCExplodingRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {
        // Cobblestone -> Cobbled Deepslate
        HCExplodingRecipeBuilder.create {
            ingredient = ingredientCreator.create(listOf(Tags.Items.STONES, Tags.Items.COBBLESTONES_NORMAL))
            result = resultCreator.create(Items.COBBLED_DEEPSLATE).withChance(fraction(1, 2))
        }.save(output)
        // Gunpowder -> Blaze Powder
        HCExplodingRecipeBuilder.create {
            ingredient = ingredientCreator.create(Tags.Items.GUNPOWDERS)
            result = resultCreator.create(Items.BLAZE_POWDER).withChance(fraction(1, 6))
        }.save(output)
        // Glass -> Quartz
        HCExplodingRecipeBuilder.create {
            ingredient = ingredientCreator.create(Tags.Items.GLASS_BLOCKS)
            result = resultCreator.create(Items.QUARTZ).withChance(fraction(1, 4))
        }.save(output)
        // Quartz Block -> Ghast Tear
        HCExplodingRecipeBuilder.create {
            ingredient = ingredientCreator.create(CommonTagPrefixes.STORAGE_BLOCK, HCMaterialContents.QUARTZ)
            result = resultCreator.create(Items.GHAST_TEAR).withChance(fraction(1, 4))
        }.save(output)

        // Diamond
        HCExplodingRecipeBuilder.create {
            ingredient = materialPart(HiiragiCoreTags.MaterialContents.COALS, CommonPartKeys.FUEL, CommonPartKeys.DUST)
            result = resultCreator.create(Items.DIAMOND).withChance(fraction(1, 64))
            recipeId suffix "_from_coal"
        }.save(output)
        // Echo Shard
        HCExplodingRecipeBuilder.create {
            ingredient = ingredientCreator.create(Items.SCULK)
            result = resultCreator.create(Items.ECHO_SHARD).withChance(fraction(1, 8))
        }.save(output)
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCExplodingRecipeProvider) {
        override fun getName(): String = "Exploding Recipes"
    }
}
