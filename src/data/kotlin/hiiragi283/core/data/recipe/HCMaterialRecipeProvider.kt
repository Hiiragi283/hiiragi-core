package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.material.CommonMaterialKeys
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.math.fraction
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput

class HCMaterialRecipeProvider(modId: String, registries: HolderLookup.Provider, output: RecipeOutput) : HTMaterialRecipeProvider(modId, registries, output) {
    override fun buildRecipes() {
        // Fuel
        nineStorageBlock(VanillaMaterialKeys.CHARCOAL)
        // Gem
        fourStorageBlock(VanillaMaterialKeys.GLOWSTONE, baseToBlock = false)
        fourStorageBlock(VanillaMaterialKeys.QUARTZ, baseToBlock = false)
        fourStorageBlock(VanillaMaterialKeys.AMETHYST, baseToBlock = false)
        fourStorageBlock(VanillaMaterialKeys.ECHO)
        // Metal
        nineNugget(VanillaMaterialKeys.NETHERITE)

        nineStorageBlock(CommonMaterialKeys.TIN)
        nineNugget(CommonMaterialKeys.TIN)
        rawStorageBlock(CommonMaterialKeys.TIN)
        smeltDustToBase(CommonMaterialKeys.TIN)
        smeltRawToBase(CommonMaterialKeys.TIN, 0.7f)
        crushBaseToDust(CommonMaterialKeys.TIN)

        nineStorageBlock(CommonMaterialKeys.IRIDIUM)
        nineNugget(CommonMaterialKeys.IRIDIUM)
        rawStorageBlock(CommonMaterialKeys.IRIDIUM)
        smeltDustToBase(CommonMaterialKeys.IRIDIUM)
        smeltRawToBase(CommonMaterialKeys.IRIDIUM, 2f)
        crushBaseToDust(CommonMaterialKeys.IRIDIUM)
        // Alloy
        // Other
        fourStorageBlock(VanillaMaterialKeys.ENDER_PEARL)
        crushToDust(VanillaMaterialKeys.ENDER_PEARL, CommonPartKeys.MISC)
        crushToDust(VanillaMaterialKeys.OBSIDIAN, CommonPartKeys.STORAGE_BLOCK, fraction(4))
        crushToDust(VanillaMaterialKeys.OBSIDIAN, CommonPartKeys.MISC)
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : Direct(HiiragiCoreAPI.MOD_ID, packOutput, registries, ::HCMaterialRecipeProvider) {
        override fun getName(): String = "Material Recipes"
    }
}
