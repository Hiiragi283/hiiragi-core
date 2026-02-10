package hiiragi283.core.api.data.model

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TexturedModel
import net.minecraft.resources.ResourceLocation

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[HTTexturedModel]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 * @see TexturedModel
 */
object HTTexturedModels {
    //    Block    //

    @JvmField
    val CUBE_ALL: HTTexturedModel.Provider = HTTexturedModel.create(
        ModelTemplates.CUBE_ALL,
        HTIdLike::blockId.andThen(TextureMapping::cube),
    )

    @JvmStatic
    fun crop(texture: ResourceLocation): HTTexturedModel.Provider =
        HTTexturedModel.create(ModelTemplates.CROP) { TextureMapping.crop(texture) }

    @JvmStatic
    fun particleOnly(particleId: ResourceLocation): HTTexturedModel.Provider = HTTexturedModel.create(ModelTemplates.PARTICLE_ONLY) { _ ->
        TextureMapping.particle(particleId)
    }

    @JvmStatic
    fun layeredBlock(layer0: ResourceLocation, layer1: ResourceLocation): HTTexturedModel.Provider =
        HTTexturedModel.create(HTModelTemplates.LAYERED) { TextureMapping.layered(layer0, layer1) }

    //    Item    //

    @JvmField
    val FLAT_ITEM: HTTexturedModel.Provider = HTTexturedModel.create(
        ModelTemplates.FLAT_ITEM,
        HTIdLike::itemId.andThen(TextureMapping::layer0),
    )

    @JvmStatic
    fun layeredItem(layer0: ResourceLocation, layer1: ResourceLocation): HTTexturedModel.Provider =
        HTTexturedModel.create(ModelTemplates.TWO_LAYERED_ITEM) { TextureMapping.layered(layer0, layer1) }

    @JvmStatic
    fun layeredItem(layer0: ResourceLocation, layer1: ResourceLocation, layer2: ResourceLocation): HTTexturedModel.Provider =
        HTTexturedModel.create(ModelTemplates.THREE_LAYERED_ITEM) { TextureMapping.layered(layer0, layer1, layer2) }
}
