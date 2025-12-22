package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.storage.attachments.HTAttachedEnergy
import hiiragi283.core.api.storage.attachments.HTAttachedFluids
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.common.registry.register.HTDeferredDataComponentRegister
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries

object HCDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, HiiragiCoreAPI.MOD_ID)

    //    Storage    //

    @JvmField
    val ENERGY: DataComponentType<HTAttachedEnergy> = REGISTER.registerType("energy", HTAttachedEnergy.CODEC)

    @JvmField
    val FLUID: DataComponentType<HTAttachedFluids> = REGISTER.registerType("fluid", HTAttachedFluids.CODEC)

    @JvmField
    val ITEM: DataComponentType<HTAttachedItems> = REGISTER.registerType("item", HTAttachedItems.CODEC)
}
