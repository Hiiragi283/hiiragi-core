package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.resources.ResourceLocation

enum class HTBackgroundType(prefix: String) {
    BOTH("both_"),
    INPUT("input_"),
    OUTPUT("output_"),
    EXTRA_INPUT("extra_input_"),
    EXTRA_OUTPUT("extra_output_"),
    NONE(""),
    ;

    val slotTexture: ResourceLocation = HiiragiCoreAPI.id("textures", "gui", "${prefix}slot.png")
    val tankTexture: ResourceLocation = HiiragiCoreAPI.id("textures", "gui", "${prefix}tank.png")
}
