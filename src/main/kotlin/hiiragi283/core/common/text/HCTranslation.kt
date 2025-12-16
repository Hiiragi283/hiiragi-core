package hiiragi283.core.common.text

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.HTTranslation
import net.minecraft.Util

enum class HCTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    HIIRAGI_CORE("constants", "name"),
    ;

    override val translationKey: String = Util.makeDescriptionId(type, HiiragiCoreAPI.id(path.joinToString(separator = ".")))
}
