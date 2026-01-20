package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.storage.attachments.HTAttachedEnergy
import hiiragi283.core.api.storage.attachments.HTAttachedFluids
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.registry.register.HTDeferredDataComponentRegister
import hiiragi283.core.common.text.HTSimpleTranslation
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries

object HCDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, HiiragiCoreAPI.MOD_ID)

    @JvmField
    val COMPLETE_PROGRESS: DataComponentType<Int> = REGISTER.registerType("complete_progress", BiCodecs.NON_NEGATIVE_INT)

    @JvmField
    val DESCRIPTION: DataComponentType<HTTranslation> = REGISTER.registerType("description", HTSimpleTranslation.CODEC)

    @JvmField
    val MATERIAL: DataComponentType<HTMaterialKey> = REGISTER.registerType("material", HTMaterialKey.CODEC)

    //    Storage    //

    @JvmField
    val ENERGY: DataComponentType<HTAttachedEnergy> = REGISTER.registerType(HTConst.ENERGY, HTAttachedEnergy.CODEC)

    @JvmField
    val FLUID: DataComponentType<HTAttachedFluids> = REGISTER.registerType(HTConst.FLUID, HTAttachedFluids.CODEC)

    @JvmField
    val ITEM: DataComponentType<HTAttachedItems> = REGISTER.registerType(HTConst.ITEM, HTAttachedItems.CODEC)
}
