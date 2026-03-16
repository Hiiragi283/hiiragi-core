package hiiragi283.core.client.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.texture.HTTextureUtil
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.toId
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette
import net.mehvahdjukaar.moonlight.api.resources.textures.Respriter
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import kotlin.collections.iterator

data object HCMaterialTextureProvider : ResourceGenTask {
    private lateinit var lavaTexture: TextureImage

    override fun accept(manager: ResourceManager, sink: ResourceSink) {
        HTTextureUtil.templatePalette = HTTextureUtil.getOrCreateColors(HiiragiCoreAPI.id("template"), manager).getOrThrow()
        lavaTexture = TextureImage.open(manager, HTConst.MINECRAFT.toId(HTConst.BLOCK, "lava_still"))

        val contents: HTMaterialAccess = HiiragiCoreAccess.INSTANCE.registeredContents
        material(manager, sink, HTConst.BLOCK, contents.blocks::column)
        material(manager, sink, HTConst.ITEM, contents.items::column)
        molten(manager, sink)
    }

    @JvmStatic
    private inline fun <T : HTIdLike> material(
        manager: ResourceManager,
        sink: ResourceSink,
        pathPrefix: String,
        factory: (HTMaterialLike) -> Map<HTPart, T>,
    ) {
        // すべての素材に対してテクスチャの生成を試みる
        for (entry: HTMaterialManager.Entry in HiiragiCoreAccess.INSTANCE.materialManager) {
            // 生成対象がない場合はパス
            val partMap: Map<HTPart, T> = factory(entry)
            if (partMap.isEmpty()) continue
            // テクスチャを生成
            val textureSet: HTMaterialTextureSet = entry.getOrDefault(HTMaterialPropertyKeys.TEXTURE_SET)
            for ((part: HTPart, element: T) in partMap) {
                if (HTPartPropertyKeys.DISABLE_TEXTURE_GEN in part) continue
                // パレットを取得
                val palette: List<Int> = sequence {
                    if (HTPartPropertyKeys.IS_RAW in part) {
                        yield(entry[HTMaterialPropertyKeys.TEXTURE_COLOR_RAW] ?: entry.getId().withPrefix("raw_"))
                    }
                    yield(entry[HTMaterialPropertyKeys.TEXTURE_COLOR] ?: entry.getId())
                }.firstNotNullOfOrNull { HTTextureUtil.getOrCreateColors(it, manager).getOrNull() }
                    ?: run {
                        HiiragiCoreAPI.LOGGER.error("Failed to get color palette for material; ${entry.asMaterialId()}")
                        continue
                    }
                // テンプレートを取得
                val template: TextureImage = getTextureResult(manager, textureSet, part)
                    .onFailure { HiiragiCoreAPI.LOGGER.error("Failed to get template image for ${part.name}") }
                    .getOrNull()
                    ?: continue
                copyAndApplyColor(
                    sink,
                    element.getId().withPrefix("$pathPrefix/"),
                    palette,
                    template,
                )
            }
        }
    }

    @JvmStatic
    private fun molten(manager: ResourceManager, sink: ResourceSink) {
        // すべての素材に対してテクスチャの生成を試みる
        for (entry: HTMaterialManager.Entry in HiiragiCoreAccess.INSTANCE.materialManager) {
            val molten: HTIdLike = HiiragiCoreAccess.INSTANCE.registeredFluids[HTFluidPart.MOLTEN, entry] ?: continue
            // パレットを取得
            var palette: Palette = HTTextureUtil
                .getOrCreatePalette(entry[HTMaterialPropertyKeys.TEXTURE_COLOR] ?: entry.getId(), manager)
                .getOrNull()
                ?: continue
            palette = Palette.fromArc(palette.elementAt(1).lab(), palette.elementAt(palette.size - 1).lab(), 9)
            HiiragiCoreAPI.LOGGER.debug(
                "Molten {} palette: {}",
                entry.asMaterialId(),
                palette.joinToString(separator = ",", transform = { it.value().toHexString() }),
            )
            // テンプレートを取得
            val template: TextureImage = lavaTexture.makeCopy()
            val respriter: Respriter = Respriter.of(template)
            val newImage: TextureImage = respriter.recolor(palette)
            sink.addTexture(molten.blockId, newImage)
        }
    }

    @JvmStatic
    private fun getTextureResult(manager: ResourceManager, textureSet: HTMaterialTextureSet, part: HTPart): Result<TextureImage> {
        val name: String = part[HTPartPropertyKeys.TEXTURE_ICON] ?: part.name
        val id: ResourceLocation = HiiragiCoreAPI.id("material_set", textureSet.name, "$name.png")
        return runCatching { TextureImage.open(manager, id) }
            .recoverCatching { throwable: Throwable ->
                val parentSet: HTMaterialTextureSet = textureSet.parent ?: throw throwable
                getTextureResult(manager, parentSet, part).getOrThrow()
            }
    }

    @JvmStatic
    private fun copyAndApplyColor(
        sink: ResourceSink,
        id: ResourceLocation,
        palette: List<Int>,
        template: TextureImage,
    ) {
        val image: TextureImage = template.makeCopy()
        for ((index: Int, pixels: Set<Pair<Int, Int>>) in createTemplate(template)) {
            for ((x: Int, y: Int) in pixels) {
                image.setPixel(x, y, palette[index])
            }
        }
        sink.addTexture(id, image)
    }

    @JvmStatic
    private fun createTemplate(image: TextureImage): Map<Int, Set<Pair<Int, Int>>> = buildMap {
        for (x: Int in (0..<image.imageWidth())) {
            for (y: Int in (0..<image.imageHeight())) {
                val index: Int = HTTextureUtil.templatePalette.indexOf(image.getPixel(x, y))
                if (index >= 0) {
                    this[index] = (this[index]?.plus(x to y) ?: setOf(x to y))
                }
            }
        }
    }
}
