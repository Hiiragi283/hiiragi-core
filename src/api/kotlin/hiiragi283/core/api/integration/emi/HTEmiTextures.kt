package hiiragi283.core.api.integration.emi

import dev.emi.emi.api.render.EmiTexture
import hiiragi283.core.api.gui.HTBackgroundType

object HTEmiTextures {
    @JvmField
    val SLOT_TEXTURES: Map<HTBackgroundType, EmiTexture> = HTBackgroundType.entries.associateWith { type: HTBackgroundType ->
        EmiTexture(type.slotTexture, 0, 0, 18, 18, 18, 18, 18, 18)
    }

    @JvmField
    val TANK_TEXTURES: Map<HTBackgroundType, EmiTexture> = HTBackgroundType.entries.associateWith { type: HTBackgroundType ->
        val width = 16
        val height: Int = 18 * 3 - 2
        EmiTexture(type.tankTexture, 0, 0, width, height, width, height, width, height)
    }
}
