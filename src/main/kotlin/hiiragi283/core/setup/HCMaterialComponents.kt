package hiiragi283.core.setup

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.Text
import hiiragi283.core.impl.registry.HTDeferredDataComponentRegister
import net.minecraft.core.component.DataComponentType

data object HCMaterialComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(HCRegistries.Keys.MATERIAL_COMPONENT_TYPE, HiiragiCoreAPI.MOD_ID)

    @JvmField
    val MATERIAL_NAME: DataComponentType<Text> = REGISTER.registerType("material_name", VanillaBiCodecs.TEXT)
}
