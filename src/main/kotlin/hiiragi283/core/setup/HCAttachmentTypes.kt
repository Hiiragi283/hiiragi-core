package hiiragi283.core.setup

import hiiragi283.core.common.world.HCInWorldRecipeCaches
import net.neoforged.neoforge.attachment.AttachmentType

data object HCAttachmentTypes {
    @JvmField
    val IN_WORLD_RECIPE_CACHES: AttachmentType<HCInWorldRecipeCaches> = AttachmentType.builder(::HCInWorldRecipeCaches).build()
}
