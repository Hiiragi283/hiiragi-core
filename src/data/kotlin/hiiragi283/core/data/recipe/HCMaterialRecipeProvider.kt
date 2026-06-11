package hiiragi283.core.data.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.material.CommonMaterialKeys
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.math.fraction
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput

class HCMaterialRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTMaterialRecipeProvider(packOutput, future, HiiragiCoreAPI.MOD_ID) {
    override fun buildRecipes() {
        // Fuel
        nineStorageBlock(VanillaMaterialKeys.CHARCOAL)

        crushBaseToDust(VanillaMaterialKeys.COAL)
        crushBaseToDust(VanillaMaterialKeys.CHARCOAL)
        // Gem
        fourStorageBlock(VanillaMaterialKeys.GLOWSTONE, baseToBlock = false)
        fourStorageBlock(VanillaMaterialKeys.QUARTZ, baseToBlock = false)
        fourStorageBlock(VanillaMaterialKeys.AMETHYST, baseToBlock = false)
        fourStorageBlock(VanillaMaterialKeys.ECHO)

        crushBaseToDust(VanillaMaterialKeys.LAPIS)
        crushBaseToDust(VanillaMaterialKeys.QUARTZ)
        crushBaseToDust(VanillaMaterialKeys.AMETHYST)
        crushBaseToDust(VanillaMaterialKeys.DIAMOND)
        crushBaseToDust(VanillaMaterialKeys.EMERALD)
        crushBaseToDust(VanillaMaterialKeys.ECHO)
        crushBaseToDust(VanillaMaterialKeys.PRISMARINE)
        // Metal
        crushBaseToDust(VanillaMaterialKeys.COPPER)
        crushBaseToDust(VanillaMaterialKeys.IRON)
        crushBaseToDust(VanillaMaterialKeys.GOLD)

        metals(CommonMaterialKeys.TIN, 0.7f)
        metals(CommonMaterialKeys.IRIDIUM, 2f)
        metals(CommonMaterialKeys.PLATINUM, 2f)
        metals(CommonMaterialKeys.LEAD, 0.7f)
        // Alloy
        nineNugget(VanillaMaterialKeys.NETHERITE)

        crushBaseToDust(VanillaMaterialKeys.NETHERITE)
        // Other
        fourStorageBlock(VanillaMaterialKeys.ENDER_PEARL)

        crushToDust(VanillaMaterialKeys.OBSIDIAN, CommonPartKeys.MISC)
        crushToDust(VanillaMaterialKeys.ENDER_PEARL, CommonPartKeys.MISC)
        crushToDust(VanillaMaterialKeys.BLAZE, CommonPartKeys.ROD, fraction(6))
        crushToDust(VanillaMaterialKeys.BREEZE, CommonPartKeys.ROD, fraction(6))
    }

    override fun getName(): String = "Material Recipes"
}
