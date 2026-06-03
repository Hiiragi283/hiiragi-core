package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCMaterialContents
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.math.fraction
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput

class HCMaterialRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTMaterialRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {
        // Base <-> Storage Block
        nineStorageBlock(HCMaterialContents.CHARCOAL)

        fourStorageBlock(HCMaterialContents.GLOWSTONE, baseToBlock = false)
        fourStorageBlock(HCMaterialContents.QUARTZ, baseToBlock = false)
        fourStorageBlock(HCMaterialContents.AMETHYST, baseToBlock = false)
        fourStorageBlock(HCMaterialContents.ECHO)

        fourStorageBlock(HCMaterialContents.ENDER_PEARL)

        nineStorageBlock(HCMaterialContents.IRIDIUM)
        // Base <-> Nugget
        nineNugget(HCMaterialContents.NETHERITE)

        nineNugget(HCMaterialContents.IRIDIUM)
        // XX -> Dust
        crushToDust(HCMaterialContents.ENDER_PEARL, CommonPartKeys.MISC)
        crushToDust(HCMaterialContents.OBSIDIAN, CommonPartKeys.STORAGE_BLOCK, fraction(4))
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCMaterialRecipeProvider) {
        override fun getName(): String = "Material Recipes"
    }
}
