package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCMaterialContents
import hiiragi283.lib.data.recipe.HTMaterialRecipeProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput

class HCMaterialRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTMaterialRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {
        // Base <-> Storage Block
        nineBaseToBlock(HCMaterialContents.CHARCOAL)
        blockToNineBase(HCMaterialContents.CHARCOAL)

        blockToFourBase(HCMaterialContents.GLOWSTONE)
        blockToFourBase(HCMaterialContents.QUARTZ)
        blockToFourBase(HCMaterialContents.AMETHYST)
        fourBaseToBlock(HCMaterialContents.ECHO)
        blockToFourBase(HCMaterialContents.ECHO)

        nineBaseToBlock(HCMaterialContents.ENDER_PEARL)
        blockToNineBase(HCMaterialContents.ENDER_PEARL)
        // Base <-> Nugget
        nuggetToBase(HCMaterialContents.NETHERITE)
        baseToNugget(HCMaterialContents.NETHERITE)
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCMaterialRecipeProvider) {
        override fun getName(): String = "Material Recipes"
    }
}
