package hiiragi283.core.common.text

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.resource.toDescriptionKey
import hiiragi283.core.api.text.HTTranslation

enum class HCTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    HIIRAGI_CORE(HTConst.CONSTANTS, "name"),

    // Block
    WARPED_WART(HTConst.DESCRIPTION, "warped_wart"),

    // Creative Mode Tab
    CREATIVE_TAB_MATERIAL(HTConst.ITEM_GROUP, "material"),
    CREATIVE_TAB_EQUIPMENT(HTConst.ITEM_GROUP, "equipment"),

    // GUI
    MIN_POWER("gui", "min_power"),

    // Item
    AMBROSIA(HTConst.DESCRIPTION, "ambrosia"),
    ANCIENT_UPGRADE(HTConst.DESCRIPTION, "ancient_upgrade"),
    ELDER_HEART(HTConst.DESCRIPTION, "elder_heart"),
    ELDRITCH_EGG(HTConst.DESCRIPTION, "eldritch_heart"),
    ETERNAL_UPGRADE(HTConst.DESCRIPTION, "eternal_upgrade"),
    EXPERIENCE_TOME(HTConst.DESCRIPTION, "experience_tome"),
    IRIDESCENT_POWDER(HTConst.DESCRIPTION, "iridescent_powder"),
    RAW_RUBBER(HTConst.DESCRIPTION, "raw_rubber"),
    SLOT_COVER(HTConst.DESCRIPTION, "slot_cover"),
    TRADER_CATALOG(HTConst.DESCRIPTION, "trader_catalog"),

    ETERNAL_PICKAXE(HTConst.ITEM, "eternal_pickaxe"),

    // Upgrade
    ANCIENT_UPGRADE_APPLIES_TO(HTConst.UPGRADE, "ancient_metal", "applies_to"),
    ANCIENT_UPGRADE_INGREDIENTS(HTConst.UPGRADE, "ancient_metal", "ingredients"),
    ANCIENT_UPGRADE_DESC(HTConst.UPGRADE, "ancient_metal"),
    ANCIENT_UPGRADE_BASE_SLOT_DESCRIPTION(HTConst.UPGRADE, "ancient_metal", "base_slot_description"),
    ANCIENT_UPGRADE_ADDITIONS_SLOT_DESCRIPTION(HTConst.UPGRADE, "ancient_metal", "additions_slot_description"),

    ETERNAL_UPGRADE_APPLIES_TO(HTConst.UPGRADE, "eternal_upgrade", "applies_to"),
    ETERNAL_UPGRADE_INGREDIENTS(HTConst.UPGRADE, "eternal_upgrade", "ingredients"),
    ETERNAL_UPGRADE_DESC(HTConst.UPGRADE, "eternal_upgrade"),
    ETERNAL_UPGRADE_BASE_SLOT_DESCRIPTION(HTConst.UPGRADE, "eternal_upgrade", "base_slot_description"),
    ETERNAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION(HTConst.UPGRADE, "eternal_upgrade", "additions_slot_description"),
    ;

    override val translationKey: String = HiiragiCoreAPI.id(path.joinToString(separator = ".")).toDescriptionKey(type)
}
