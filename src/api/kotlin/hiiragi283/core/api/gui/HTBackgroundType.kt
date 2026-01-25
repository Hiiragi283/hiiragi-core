package hiiragi283.core.api.gui

import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.resources.ResourceLocation

enum class HTBackgroundType(val isInput: Boolean, val isOutput: Boolean) {
    BOTH(true, true),
    INPUT(true, false),
    OUTPUT(false, true),
    EXTRA_INPUT(true, false),
    EXTRA_OUTPUT(false, true),
    NONE(false, false),
    ;

    val slotTexture: ResourceLocation = HiiragiCoreAPI.id("textures", "gui", "slot", "${name.lowercase()}.png")
    val tankTexture: ResourceLocation = HiiragiCoreAPI.id("textures", "gui", "tank", "${name.lowercase()}.png")
}
