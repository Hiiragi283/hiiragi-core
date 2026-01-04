package hiiragi283.core.common.text

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.HTTranslation
import net.minecraft.Util

enum class HCTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    HIIRAGI_CORE("constants", "name"),

    // Block
    WARPED_WART("description", "warped_wart"),

    // Item
    AMBROSIA("description", "ambrosia"),
    ELDER_HEART("description", "elder_heart"),
    ETERNAL_TICKET("description", "eternal_ticket"),
    IRIDESCENT_POWDER("description", "iridescent_powder"),
    SLOT_COVER("description", "slot_cover"),
    TRADER_CATALOG("description", "trader_catalog"),
    ;

    override val translationKey: String = Util.makeDescriptionId(type, HiiragiCoreAPI.id(path.joinToString(separator = ".")))
}
