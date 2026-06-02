package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.HTConstants
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.registry.HTDeferredDataComponentRegister
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.fluids.FluidStackTemplate

data object HCDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, HiiragiCoreAPI.MOD_ID)

    @JvmField
    val BOTTLE_TYPE: DataComponentType<HTBottleType> = REGISTER.registerType("bottle_type", HTBottleType.CODEC, HTBottleType.STREAM_CODEC)

    @JvmField
    val FLUID: DataComponentType<FluidStackTemplate> = REGISTER.registerType(HTConstants.FLUID, FluidStackTemplate.CODEC, FluidStackTemplate.STREAM_CODEC)
}
