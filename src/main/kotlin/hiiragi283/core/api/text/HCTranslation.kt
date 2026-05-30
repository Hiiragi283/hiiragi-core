package hiiragi283.core.api.text

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.toDescriptionKey
import hiiragi283.lib.text.HTTranslation

enum class HCTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    HIIRAGI_CORE(HTConstants.CONSTANTS, "name"),

    // Block

    // Item
    ETERNAL_PICKAXE(HTConstants.ITEM, "eternal_pickaxe"),

    // Upgrade
    ETERNAL_UPGRADE_APPLIES_TO(HTConstants.UPGRADE, "eternal_upgrade", "applies_to"),
    ETERNAL_UPGRADE_INGREDIENTS(HTConstants.UPGRADE, "eternal_upgrade", "ingredients"),
    ETERNAL_UPGRADE_BASE_SLOT_DESCRIPTION(HTConstants.UPGRADE, "eternal_upgrade", "base_slot_description"),
    ETERNAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION(HTConstants.UPGRADE, "eternal_upgrade", "additions_slot_description"),
    ;

    override val translationKey: String = HiiragiCoreAPI.id(path.joinToString(separator = ".")).toDescriptionKey(type)
}
