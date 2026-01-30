package hiiragi283.core.data.client.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCEntityTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.data.PackOutput

class HCEnglishLangProvider(output: PackOutput) :
    HTLangProvider.English(output, HiiragiCoreAPI.MOD_ID),
    HCLangProvider {
    override fun addTranslations() {
        addMaterials()
        // Block
        add(HCBlocks.WARPED_WART, "Warped Wart")

        // Entity
        add(HCEntityTypes.ELDRITCH_EGG, "Thrown Eldritch Egg")

        // Fluid
        addFluid(HCFluids.EXPERIENCE, "Liquid Experience")
        addFluid(HCFluids.HONEY, "Honey")
        addFluid(HCFluids.MUSHROOM_STEW, "Mushroom Stew")

        addFluid(HCFluids.LATEX, "Latex")
        addFluid(HCFluids.BLOOD, "Blood")
        addFluid(HCFluids.MEAT, "Meat")

        addFluid(HCFluids.MOLTEN_GLASS, "Molten Glass")
        addFluid(HCFluids.MOLTEN_PLASTIC, "Molten Plastic")
        addFluid(HCFluids.MOLTEN_RUBBER, "Molten Rubber")
        addFluid(HCFluids.MOLTEN_CRIMSON_CRYSTAL, "Crimson Blood")
        addFluid(HCFluids.MOLTEN_WARPED_CRYSTAL, "Dew of the Warp")
        addFluid(HCFluids.MOLTEN_ELDRITCH, "Eldritch Flux")
        addFluid(HCFluids.MOLTEN_OMINOUS_METAL, "Ominous Flux")

        // Item
        add(HCItems.BAMBOO_CHARCOAL, "Bamboo Charcoal")
        add(HCItems.COMPRESSED_SAWDUST, "Compressed Sawdust")
        add(HCItems.POLYMER_RESIN, "Polymer Resin")
        add(HCItems.RAW_RUBBER, "Raw Rubber")
        add(HCItems.STEEL_COMPOUND, "Steel Compound")
        add(HCItems.SYNTHETIC_LEATHER, "Synthetic Leather")

        add(HCItems.LUMINOUS_PASTE, "Luminous Paste")
        add(HCItems.MAGMA_SHARD, "Magma Shard")
        add(HCItems.ELDER_HEART, "Elder Heart")
        add(HCItems.WITHER_DOLL, "Wither Doll")
        add(HCItems.WITHER_STAR, "Wither Star")

        add(HCItems.ELDRITCH_EGG, "Eldritch Egg")
        add(HCItems.FLUID_FILTER, "Fluid Filter")
        add(HCItems.ITEM_FILTER, "Item Filter")
        add(HCItems.SLOT_COVER, "Slot Cover")
        add(HCItems.TRADER_CATALOG, "Trader's Catalog")

        add(HCItems.IRIDESCENT_POWDER, "Iridescent Powder")
        add(HCItems.AMBROSIA, "Ambrosia")
        add(HCItems.ETERNAL_UPGRADE, "Eternal Smithing Template")
        add(HCItems.ALMIGHTY_PICKAXE, "Almighty Pickaxe")

        // Recipe
        add(HCRecipeTypes.ANVIL_CRUSHING, "Anvil Crushing")
        add(HCRecipeTypes.CHARGING, "Lightning Charging")
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

        add(HTCommonTranslation.DOWN, "Down")
        add(HTCommonTranslation.UP, "Up")
        add(HTCommonTranslation.NORTH, "North")
        add(HTCommonTranslation.SOUTH, "South")
        add(HTCommonTranslation.WEST, "West")
        add(HTCommonTranslation.EAST, "East")
        // API - Error
        add(HTCommonTranslation.EMPTY_TAG_KEY, $$"Empty tag key: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_S2C, $$"Invalid packet received from server side: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_C2S, $$"Invalid packet received from client side: %1$s")

        add(HTCommonTranslation.MISSING_SERVER, "Could not find current server")
        add(HTCommonTranslation.MISSING_REGISTRY, $$"Missing registry: %1$s")
        add(HTCommonTranslation.MISSING_KEY, $$"Missing key: %1$s")
        // API - GUI
        add(HTCommonTranslation.PROGRESS, $$"Progress: %1$s %%")
        add(HTCommonTranslation.SECONDS, $$"%1$s sec (%2$s ticks)")
        // API - Item
        add(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"Always has at least %1$s")
        add(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION, "Press Shift to show description")
        add(HTCommonTranslation.TOOLTIP_SHOW_DETAILS, "Press Ctrl to show details")

        add(HTCommonTranslation.DATAPACK_WIP, "Enables work in progress contents")

        // Mod
        add(HCTranslation.HIIRAGI_CORE, "Hiiragi Core")

        add(HCTranslation.WARPED_WART, "Clears one bad effect randomly when eaten.")

        add(HCTranslation.CREATIVE_TAB_MATERIAL, "Hiiragi Core - Material")
        add(HCTranslation.CREATIVE_TAB_EQUIPMENT, "Hiiragi Core - Equipment")

        add(HCTranslation.MOLTEN_METAL, "Molten %s")
        add(HCTranslation.MOLTEN_METAL_BUCKET, "Molten %s Bucket")

        add(HCTranslation.MIN_POWER, "Minimum Explosion Power: %s")

        add(HCTranslation.AMBROSIA, "ALWAYS EDIBLE and NOT CONSUMED!")
        add(HCTranslation.ELDER_HEART, "Dropped from Elder Guardian.")
        add(HCTranslation.ELDRITCH_EGG, "Can be throwable by right-click，and capture mobs when hit.")
        add(HCTranslation.ETERNAL_UPGRADE, "Dropped from Ender Dragon.")
        add(HCTranslation.IRIDESCENT_POWDER, "Do not expire by time over or any damage.")
        add(HCTranslation.SLOT_COVER, "Ignored by recipes when placed in machine slot.")
        add(HCTranslation.TRADER_CATALOG, "Dropped from Wandering Trader. Right-click to trade with merchant.")

        add(HCTranslation.ETERNAL_PICKAXE, "Eternal Pickaxe")

        add(HCTranslation.ETERNAL_UPGRADE_APPLIES_TO, "Any Equipment")
        add(HCTranslation.ETERNAL_UPGRADE_INGREDIENTS, "Iridium Ingot")
        add(HCTranslation.ETERNAL_UPGRADE_DESC, "Unbreakable Upgrade")
        add(HCTranslation.ETERNAL_UPGRADE_BASE_SLOT_DESCRIPTION, "Add any armor, weapon, or tool")
        add(HCTranslation.ETERNAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "Add Iridium Ingot")
    }
}
