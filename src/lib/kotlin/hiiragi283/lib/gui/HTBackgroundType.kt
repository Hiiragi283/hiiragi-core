package hiiragi283.lib.gui

import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.toId
import net.minecraft.resources.Identifier

/**
 * スロットやタンクの背景の種類を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTBackgroundType(val isInput: Boolean, val isOutput: Boolean) {
    BOTH(true, true),
    INPUT(true, false),
    OUTPUT(false, true),
    EXTRA_INPUT(true, false),
    EXTRA_OUTPUT(false, true),
    NONE(false, false),
    ;

    val slotTexture: Identifier = HTConstants.MOD_ID.toId(HTConstants.TEXTURES, HTConstants.GUI, HTConstants.SLOT, "${name.lowercase()}.png")
    val tankTexture: Identifier = HTConstants.MOD_ID.toId(HTConstants.TEXTURES, HTConstants.GUI, HTConstants.TANK, "${name.lowercase()}.png")
}
