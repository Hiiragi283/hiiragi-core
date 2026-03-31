package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.text.HTSimpleTranslation
import hiiragi283.core.impl.registry.HTDeferredDataComponentRegister
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.fluids.SimpleFluidContent

data object HCDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, HiiragiCoreAPI.MOD_ID)

    @JvmField
    val DESCRIPTION: DataComponentType<HTTranslation> = REGISTER.registerType("description", HTSimpleTranslation.CODEC)

    //    Transfer    //

    /**
     * @see net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler
     */
    @JvmField
    val ENERGY: DataComponentType<Int> = REGISTER.registerType(HTConst.ENERGY, BiCodecs.NON_NEGATIVE_INT)

    /**
     * @see net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler
     */
    @JvmField
    val FLUID: DataComponentType<SimpleFluidContent> = REGISTER.registerType(
        HTConst.FLUID,
        SimpleFluidContent.CODEC,
        SimpleFluidContent.STREAM_CODEC,
    )
}
