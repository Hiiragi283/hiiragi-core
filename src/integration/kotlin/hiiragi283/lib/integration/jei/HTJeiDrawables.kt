package hiiragi283.lib.integration.jei

import hiiragi283.lib.gui.HTBackgroundType
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import java.util.EnumMap

/**
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
object HTJeiDrawables {
    @JvmStatic
    private val SLOTS: MutableMap<HTBackgroundType, IDrawable> = EnumMap(HTBackgroundType::class.java)

    @JvmStatic
    private val TANKS: MutableMap<HTBackgroundType, IDrawable> = EnumMap(HTBackgroundType::class.java)

    @JvmStatic
    fun getSlot(type: HTBackgroundType, guiHelper: IGuiHelper): IDrawable = SLOTS.computeIfAbsent(type) { typeIn: HTBackgroundType ->
        guiHelper
            .drawableBuilder(typeIn.slotTexture, 0, 0, 18, 18)
            .setTextureSize(18, 18)
            .build()
    }

    @JvmStatic
    fun getTank(type: HTBackgroundType, guiHelper: IGuiHelper): IDrawable = TANKS.computeIfAbsent(type) { typeIn: HTBackgroundType ->
        guiHelper
            .drawableBuilder(typeIn.tankTexture, 0, 0, 18, 18 * 3)
            .setTextureSize(18, 18 * 3)
            .build()
    }
}
