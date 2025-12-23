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
    IRIDESCENT_POWDER("description", "iridescent_powder"),
    ;

    override val translationKey: String = Util.makeDescriptionId(type, HiiragiCoreAPI.id(path.joinToString(separator = ".")))
}
