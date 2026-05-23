package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTDeferredAttachmentRegister
import hiiragi283.core.common.world.HCInWorldRecipeCaches
import net.neoforged.neoforge.attachment.AttachmentType

data object HCAttachmentTypes {
    @JvmField
    val REGISTER = HTDeferredAttachmentRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val IN_WORLD_RECIPE_CACHES: AttachmentType<HCInWorldRecipeCaches> =
        REGISTER.registerType("in_world_recipe_caches", AttachmentType.builder(::HCInWorldRecipeCaches))
}
