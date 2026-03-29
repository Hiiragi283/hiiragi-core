package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.common.registry.HTDeferredAttachmentType
import hiiragi283.core.common.registry.register.HTDeferredAttachmentRegister
import hiiragi283.core.common.world.HCInWorldRecipeCaches
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.attachment.IAttachmentHolder

object HCAttachmentTypes {
    @JvmField
    val REGISTER = HTDeferredAttachmentRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val EFFECT_FLAG: HTDeferredAttachmentType<Boolean> = REGISTER.registerSerializable(
        "effect_flag",
        { holder: IAttachmentHolder ->
            when (holder) {
                is LivingEntity -> false
                else -> error("Cannot apply effect flag for $holder")
            }
        },
        BiCodec.BOOL,
    )

    @JvmField
    val IN_WORLD_RECIPE_CACHES: HTDeferredAttachmentType<HCInWorldRecipeCaches> =
        REGISTER.registerSerializable("in_world_recipe_caches", ::HCInWorldRecipeCaches)
}
