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
        // Fuel
        nineStorageBlock(HCMaterialContents.CHARCOAL)
        // Gem
        fourStorageBlock(HCMaterialContents.GLOWSTONE, baseToBlock = false)
        fourStorageBlock(HCMaterialContents.QUARTZ, baseToBlock = false)
        fourStorageBlock(HCMaterialContents.AMETHYST, baseToBlock = false)
        fourStorageBlock(HCMaterialContents.ECHO)
        // Metal
        nineNugget(HCMaterialContents.NETHERITE)

        nineStorageBlock(HCMaterialContents.IRIDIUM)
        nineNugget(HCMaterialContents.IRIDIUM)
        rawStorageBlock(HCMaterialContents.IRIDIUM)
        smeltDustToBase(HCMaterialContents.IRIDIUM)
        smeltRawToBase(HCMaterialContents.IRIDIUM, 2f)
        crushBaseToDust(HCMaterialContents.IRIDIUM)
        // Alloy
        // Other
        fourStorageBlock(HCMaterialContents.ENDER_PEARL)
        crushToDust(HCMaterialContents.ENDER_PEARL, CommonPartKeys.MISC)

        crushToDust(HCMaterialContents.OBSIDIAN, CommonPartKeys.STORAGE_BLOCK, fraction(4))
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCMaterialRecipeProvider) {
        override fun getName(): String = "Material Recipes"
    }
}
