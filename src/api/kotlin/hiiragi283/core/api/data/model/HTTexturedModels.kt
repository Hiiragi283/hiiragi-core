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
    fun crop(texture: ResourceLocation): HTTexturedModel = HTTexturedModel(ModelTemplates.CROP, TextureMapping.crop(texture))

    @JvmStatic
    fun particleOnly(particleId: ResourceLocation): HTTexturedModel =
        HTTexturedModel(ModelTemplates.PARTICLE_ONLY, TextureMapping.particle(particleId))

    @JvmStatic
    fun layeredBlock(layer0: ResourceLocation, layer1: ResourceLocation): HTTexturedModel =
        HTTexturedModel(HTModelTemplates.LAYERED, TextureMapping.layered(layer0, layer1))

    //    Item    //

    @JvmField
    val FLAT_ITEM: HTTexturedModel.Provider = HTTexturedModel.create(
        ModelTemplates.FLAT_ITEM,
        HTIdLike::itemId.andThen(TextureMapping::layer0),
    )

    @JvmField
    val FLAT_HANDHELD_ITEM: HTTexturedModel.Provider = HTTexturedModel.create(
        ModelTemplates.FLAT_HANDHELD_ITEM,
        HTIdLike::itemId.andThen(TextureMapping::layer0),
    )

    @JvmStatic
    fun flatAlt(layer0: ResourceLocation): HTTexturedModel = HTTexturedModel(ModelTemplates.FLAT_ITEM, TextureMapping.layer0(layer0))

    @JvmStatic
    fun layeredItem(layer0: ResourceLocation, layer1: ResourceLocation): HTTexturedModel =
        HTTexturedModel(ModelTemplates.TWO_LAYERED_ITEM, TextureMapping.layered(layer0, layer1))

    @JvmStatic
    fun layeredItem(layer0: ResourceLocation, layer1: ResourceLocation, layer2: ResourceLocation): HTTexturedModel =
        HTTexturedModel(ModelTemplates.THREE_LAYERED_ITEM, TextureMapping.layered(layer0, layer1, layer2))
}
