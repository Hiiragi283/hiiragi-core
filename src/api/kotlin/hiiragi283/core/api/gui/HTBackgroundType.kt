package hiiragi283.core.api.gui

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.resources.ResourceLocation

/**
 * スロットやタンクの背景の種類を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
enum class HTBackgroundType(val isInput: Boolean, val isOutput: Boolean) {
    BOTH(true, true),
    INPUT(true, false),
    OUTPUT(false, true),
    EXTRA_INPUT(true, false),
    EXTRA_OUTPUT(false, true),
    NONE(false, false),
    ;

    val slotTexture: ResourceLocation = HiiragiCoreAPI.id(HTConst.TEXTURES, HTConst.GUI, HTConst.SLOT, "${name.lowercase()}.png")
    val tankTexture: ResourceLocation = HiiragiCoreAPI.id(HTConst.TEXTURES, HTConst.GUI, HTConst.TANK, "${name.lowercase()}.png")
}
