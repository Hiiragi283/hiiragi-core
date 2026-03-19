package hiiragi283.core.data.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLanguageProvider
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.data.PackOutput

class HCEnglishLangProvider(output: PackOutput) :
    HTLanguageProvider.English(output, HiiragiCoreAPI.MOD_ID),
    HCLangProvider {
    override fun addTranslations() {
        // Block
        add(HCBlocks.WARPED_WART, "Warped Wart")
        // Item
        add(HCItems.ALMIGHTY_PICKAXE, "Almighty Pickaxe")

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

        add(HTCommonTranslation.CHANCE_CONSUME, $$"Consumption Chance: %1$s %%")
        add(HTCommonTranslation.CHANCE_PRODUCE, $$"Production Chance: %1$s %%")
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
        add(HCTranslation.ANCIENT_UPGRADE, "Dropped from Warden.")
        add(HCTranslation.ELDER_HEART, "Dropped from Elder Guardian.")
        add(HCTranslation.ELDRITCH_EGG, "Can be throwable by right-click，and capture mobs when hit.")
        add(HCTranslation.ETERNAL_UPGRADE, "Dropped from Ender Dragon.")
        add(HCTranslation.EXPERIENCE_TOME, "Right-click to store experience, or release when shifting.")
        add(HCTranslation.IRIDESCENT_POWDER, "Do not expire by time over or any damage.")
        add(HCTranslation.RAW_RUBBER, "Dropped from placed Latex fluid or Latex Cauldron.")
        add(HCTranslation.SLOT_COVER, "Ignored by recipes when placed in machine slot.")
        add(HCTranslation.TRADER_CATALOG, "Dropped from Wandering Trader. Right-click to trade with merchant.")

        add(HCTranslation.ETERNAL_PICKAXE, "Eternal Pickaxe")

        add(HCTranslation.ANCIENT_UPGRADE_APPLIES_TO, "Diamond Equipment")
        add(HCTranslation.ANCIENT_UPGRADE_INGREDIENTS, "Ancient Metal Ingot")
        add(HCTranslation.ANCIENT_UPGRADE_DESC, "Ancient Upgrade")
        add(HCTranslation.ANCIENT_UPGRADE_BASE_SLOT_DESCRIPTION, "Add ancient metal armor, weapon, or tool")
        add(HCTranslation.ANCIENT_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "Add Ancient Metal Ingot")

        add(HCTranslation.ETERNAL_UPGRADE_APPLIES_TO, "Any Equipment")
        add(HCTranslation.ETERNAL_UPGRADE_INGREDIENTS, "Iridium Ingot")
        add(HCTranslation.ETERNAL_UPGRADE_DESC, "Unbreakable Upgrade")
        add(HCTranslation.ETERNAL_UPGRADE_BASE_SLOT_DESCRIPTION, "Add any armor, weapon, or tool")
        add(HCTranslation.ETERNAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "Add Iridium Ingot")
    }
}
