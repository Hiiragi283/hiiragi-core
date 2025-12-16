package hiiragi283.core.data.client.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.client.emi.category.HCEmiRecipeCategories
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.data.PackOutput

class HCEnglishLangProvider(output: PackOutput) : HTLangProvider.English(output, HiiragiCoreAPI.MOD_ID) {
    override fun addTranslations() {
        // Material
        HCMaterialTranslations.addTranslations(this)

        // Block
        add(HCBlocks.DRYING_LACK, "Drying Lack")

        // Fluid
        addFluid(HCFluids.HONEY, "Honey")
        addFluid(HCFluids.MUSHROOM_STEW, "Mushroom Stew")

        addFluid(HCFluids.LATEX, "Latex")
        addFluid(HCFluids.CRIMSON_SAP, "Crimson Sap")
        addFluid(HCFluids.WARPED_SAP, "Warped Sap")

        // Item
        add(HCItems.COAL_CHIP, "Coal Chip")
        add(HCItems.COAL_CHUNK, "Coal Chunk")
        add(HCItems.SYNTHETIC_LEATHER, "Synthetic Leather")
        add(HCItems.TAR, "Tar")

        add(HCItems.CRYOGEN_POWDER, "Cryogen Powder")
        add(HCItems.LUMINOUS_PASTE, "Luminous Paste")
        add(HCItems.MAGMA_SHARD, "Magma Shard")
        add(HCItems.ELDER_HEART, "Elder Heart")
        add(HCItems.WITHER_STAR, "Wither Star")

        add(HCItems.IRIDESCENT_POWDER, "Iridescent Powder")

        // Translation
        add(HCTranslation.HIIRAGI_CORE, "Hiiragi Core")

        // EMI
        add(HCEmiRecipeCategories.CRUSHING, "Crushing")
        add(HCEmiRecipeCategories.DRYING, "Drying")
    }
}
