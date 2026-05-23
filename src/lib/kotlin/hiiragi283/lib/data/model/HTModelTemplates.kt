package hiiragi283.lib.data.model

import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder

/**
 * @see TexturedModel
 */
data object HTModelTemplates {
    @JvmField
    val FLUID_BLOCK: ExtendedModelTemplate = ExtendedModelTemplateBuilder.builder()
        .requiredTextureSlot(TextureSlot.PARTICLE)
        .build()
}
