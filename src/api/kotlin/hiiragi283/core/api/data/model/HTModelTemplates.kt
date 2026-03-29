package hiiragi283.core.api.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.resource.toId
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.core.Direction
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.client.model.generators.template.ElementBuilder
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder
import net.neoforged.neoforge.client.model.generators.template.FaceBuilder

/**
 * @see net.minecraft.client.data.models.model.ModelTemplates
 */
object HTModelTemplates {
    //    Block    //

    @JvmStatic
    private val BLOCK_BLOCK: Identifier = HTConst.MINECRAFT.toId(HTConst.BLOCK, HTConst.BLOCK)

    @JvmField
    val CUBE_ALL_TINTED: ModelTemplate = ExtendedModelTemplateBuilder
        .builder()
        .parent(BLOCK_BLOCK)
        .requiredTextureSlot(TextureSlot.PARTICLE)
        .requiredTextureSlot(TextureSlot.ALL)
        .element { builder: ElementBuilder ->
            builder
                .from(0f, 0f, 0f)
                .to(16f, 16f, 16f)
                .allFaces { direction: Direction, faceBuilder: FaceBuilder ->
                    faceBuilder
                        .texture(TextureSlot.ALL)
                        .cullface(direction)
                        .tintindex(0)
                }
        }.build()

    @JvmField
    val CUBE_LAYERED: ModelTemplate = ExtendedModelTemplateBuilder
        .builder()
        .parent(BLOCK_BLOCK)
        .requiredTextureSlot(TextureSlot.PARTICLE)
        .requiredTextureSlot(TextureSlot.LAYER0)
        .requiredTextureSlot(TextureSlot.LAYER1)
        .element { builder: ElementBuilder ->
            builder
                .from(0f, 0f, 0f)
                .to(16f, 16f, 16f)
                .allFaces { direction: Direction, faceBuilder: FaceBuilder ->
                    faceBuilder
                        .texture(TextureSlot.LAYER0)
                        .cullface(direction)
                        .tintindex(0)
                }
        }.element { builder: ElementBuilder ->
            builder
                .from(-0.01f, -0.01f, -0.01f)
                .to(16.01f, 16.01f, 16.01f)
                .allFaces { direction: Direction, faceBuilder: FaceBuilder ->
                    faceBuilder
                        .texture(TextureSlot.LAYER1)
                        .cullface(direction)
                        .tintindex(0)
                }
        }.build()

    //    Item    //
}
