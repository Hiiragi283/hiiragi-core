package hiiragi283.core.data.model

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.HTConstants
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder

/**
 * @see TexturedModel
 */
data object HCModelTemplates {
    @JvmField
    val CAULDRON: ExtendedModelTemplate = ExtendedModelTemplateBuilder.builder()
        .parent(HiiragiCoreAPI.id(HTConstants.BLOCK, "cauldron_template"))
        .requiredTextureSlot(TextureSlot.TOP)
        .requiredTextureSlot(TextureSlot.SIDE)
        .requiredTextureSlot(TextureSlot.BOTTOM)
        .requiredTextureSlot(TextureSlot.INSIDE)
        .build()
}
