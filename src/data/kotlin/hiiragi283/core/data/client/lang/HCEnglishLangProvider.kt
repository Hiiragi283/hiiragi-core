package hiiragi283.core.data.client.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.data.PackOutput

class HCEnglishLangProvider(output: PackOutput) :
    HTLangProvider.English(output, HiiragiCoreAPI.MOD_ID),
    HCLangProvider {
    override fun addTranslations() {
        // Material
        HCMaterialTranslations.addTranslations(this)

        // Fluid
        addFluid(HCFluids.HONEY, "Honey")
        addFluid(HCFluids.MUSHROOM_STEW, "Mushroom Stew")

        addFluid(HCFluids.LATEX, "Latex")
        addFluid(HCFluids.CRIMSON_SAP, "Crimson Sap")
        addFluid(HCFluids.WARPED_SAP, "Warped Sap")

        addFluid(HCFluids.CRIMSON_BLOOD, "Crimson Blood")
        addFluid(HCFluids.DEW_OF_THE_WARP, "Dew of the Warp")
        addFluid(HCFluids.ELDRITCH_FLUX, "Eldritch Flux")

        // Item
        add(HCItems.COMPRESSED_SAWDUST, "Compressed Sawdust")
        add(HCItems.SYNTHETIC_LEATHER, "Synthetic Leather")
        add(HCItems.TAR, "Tar")

        add(HCItems.LUMINOUS_PASTE, "Luminous Paste")
        add(HCItems.MAGMA_SHARD, "Magma Shard")
        add(HCItems.ELDER_HEART, "Elder Heart")
        add(HCItems.WITHER_STAR, "Wither Star")

        add(HCItems.IRIDESCENT_POWDER, "Iridescent Powder")

        // Recipe
        add(HCRecipeTypes.CHARGING, "Charging")
        add(HCRecipeTypes.CRUSHING, "Crushing")
        add(HCRecipeTypes.DRYING, "Drying")
        add(HCRecipeTypes.EXPLODING, "Exploding")

        // Translation
        translation()
    }

    private fun translation() {
        addCommonTranslations(::add)

        // API - Constants
        add(HTCommonTranslation.ERROR, "Error")
        add(HTCommonTranslation.INFINITE, "Infinite")
        add(HTCommonTranslation.NONE, "None")
        add(HTCommonTranslation.EMPTY, "Empty")
        // API - Error
        add(HTCommonTranslation.EMPTY_TAG_KEY, $$"Empty tag key: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_S2C, $$"Invalid packet received from server side: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_C2S, $$"Invalid packet received from client side: %1$s")

        add(HTCommonTranslation.MISSING_SERVER, "Could not find current server")
        add(HTCommonTranslation.MISSING_REGISTRY, $$"Missing registry: %1$s")
        add(HTCommonTranslation.MISSING_KEY, $$"Missing key: %1$s")
        // API - GUI
        add(HTCommonTranslation.SECONDS, $$"%1$s sec (%2$s ticks)")
        // API - Item
        add(HTCommonTranslation.TOOLTIP_BLOCK_POS, $$"Position: [%1$s, %2$s, %3$s]")
        add(HTCommonTranslation.TOOLTIP_CHARGE_POWER, $$"Power: %1$s")
        add(HTCommonTranslation.TOOLTIP_DIMENSION, $$"Dimension: %1$s")
        add(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"Always has at least %1$s")
        add(HTCommonTranslation.TOOLTIP_LOOT_TABLE_ID, $$"Loot Table: %1$s")
        add(HTCommonTranslation.TOOLTIP_UPGRADE_TARGET, $$"Upgrade Targets: %1$s")
        add(HTCommonTranslation.TOOLTIP_UPGRADE_EXCLUSIVE, $$"Conflicting Upgrades: %1$s")

        add(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION, "Press Shift to show description")
        add(HTCommonTranslation.TOOLTIP_SHOW_DETAILS, "Press Ctrl to show details")

        add(HTCommonTranslation.DATAPACK_WIP, "Enables work in progress contents")
    }
}
