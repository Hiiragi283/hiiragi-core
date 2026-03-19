package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.registry.HTDeferredDataComponentRegister
import hiiragi283.core.common.text.HTSimpleTranslation
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries

data object HCDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, HiiragiCoreAPI.MOD_ID)

    @JvmField
    val DESCRIPTION: DataComponentType<HTTranslation> = REGISTER.registerType("description", HTSimpleTranslation.CODEC)
}
