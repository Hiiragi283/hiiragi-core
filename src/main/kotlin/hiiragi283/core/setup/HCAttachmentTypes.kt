package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.registry.HTDeferredAttachmentType
import hiiragi283.core.common.registry.register.HTDeferredAttachmentRegister
import hiiragi283.core.common.world.HCInWorldRecipeCaches

object HCAttachmentTypes {
    @JvmField
    val REGISTER = HTDeferredAttachmentRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val IN_WORLD_RECIPE_CACHES: HTDeferredAttachmentType<HCInWorldRecipeCaches> =
        REGISTER.registerSerializable("in_world_recipe_caches", ::HCInWorldRecipeCaches)
}
