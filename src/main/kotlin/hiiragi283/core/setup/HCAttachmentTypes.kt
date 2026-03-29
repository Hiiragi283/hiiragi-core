package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.transfer.item.HTEnderBundleManager
import hiiragi283.core.impl.registry.HTDeferredAttachmentRegister
import hiiragi283.core.impl.registry.HTDeferredAttachmentType

internal object HCAttachmentTypes {
    @JvmField
    val REGISTER = HTDeferredAttachmentRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val ENDER_BUNDLE: HTDeferredAttachmentType<HTEnderBundleManager> =
        REGISTER.registerSerializable("ender_bundle", ::HTEnderBundleManager)
}
