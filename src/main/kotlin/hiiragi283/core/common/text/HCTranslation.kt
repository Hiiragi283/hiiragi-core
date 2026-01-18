package hiiragi283.core.common.text

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.resource.toDescriptionKey
import hiiragi283.core.api.text.HTTranslation

enum class HCTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    HIIRAGI_CORE("constants", "name"),

    // Block
    WARPED_WART("description", "warped_wart"),

    // Creative Mode Tab
    CREATIVE_TAB_MATERIAL("itemGroup", "material"),

    // Fluid
    MOLTEN_METAL("fluid", "molten_metal"),
    MOLTEN_METAL_BUCKET("fluid", "molten_metal_bucket"),

    // Item
    AMBROSIA("description", "ambrosia"),
    ELDER_HEART("description", "elder_heart"),
    ELDRITCH_EGG("description", "eldritch_heart"),
    ETERNAL_TICKET("description", "eternal_ticket"),
    IRIDESCENT_POWDER("description", "iridescent_powder"),
    SLOT_COVER("description", "slot_cover"),
    TRADER_CATALOG("description", "trader_catalog"),
    ;

    override val translationKey: String = HiiragiCoreAPI.id(path.joinToString(separator = ".")).toDescriptionKey(type)
}
