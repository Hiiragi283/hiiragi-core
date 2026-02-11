package hiiragi283.core.client.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.HTMapLike
import hiiragi283.core.api.data.texture.HTTextureUtil
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import kotlin.collections.iterator

data object HCMaterialTextureProvider : ResourceGenTask {
    override fun accept(manager: ResourceManager, sink: ResourceSink) {
        HTTextureUtil.templatePalette = HTTextureUtil.getPalette(manager, HiiragiCoreAPI.id("template")).getOrThrow()

        with(HiiragiCoreAccess.INSTANCE.materialContents) {
            material(manager, sink, HTConst.BLOCK, ::getBlockMap)
            material(manager, sink, HTConst.ITEM, ::getItemMap)
        }
    }

    @JvmStatic
    private fun material(
        manager: ResourceManager,
        sink: ResourceSink,
        pathPrefix: String,
        factory: (HTMaterialLike) -> HTMapLike<HTTagPrefix, out HTIdLike>,
    ) {
        // すべての素材に対してテクスチャの生成を試みる
        for (entry: HTMaterialManager.Entry in HiiragiCoreAccess.INSTANCE.materialManager) {
            // 生成対象がない場合はパス
            val prefixedMap: HTMapLike<HTTagPrefix, out HTIdLike> = factory(entry)
            if (prefixedMap.isEmpty) continue
            // テクスチャを生成
            val textureSet: HTMaterialTextureSet = entry.getOrDefault(HTMaterialPropertyKeys.TEXTURE_SET)
            for ((prefix: HTTagPrefix, element: HTIdLike) in prefixedMap) {
                if (HTTagPropertyKeys.DISABLE_TEXTURE_GEN in prefix) continue
                // パレットを取得
                val palette: List<Int> = sequence {
                    if (HTTagPropertyKeys.IS_RAW in prefix) {
                        yield(entry[HTMaterialPropertyKeys.TEXTURE_COLOR_RAW] ?: entry.getId().withPrefix("raw_"))
                    }
                    yield(entry[HTMaterialPropertyKeys.TEXTURE_COLOR] ?: entry.getId())
                }.map { HTTextureUtil.getPalette(manager, it) }
                    .firstNotNullOfOrNull { it.getOrNull() }
                    ?: continue
                // テンプレートを取得
                val template: TextureImage = getTextureResult(manager, textureSet, prefix).getOrNull() ?: continue
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
    private fun getTextureResult(manager: ResourceManager, textureSet: HTMaterialTextureSet, prefix: HTTagPrefix): Result<TextureImage> {
        val name: String = prefix[HTTagPropertyKeys.TEXTURE_ICON] ?: prefix.name
        val id: ResourceLocation = HiiragiCoreAPI.id("material_set", textureSet.name, "$name.png")
        return runCatching { TextureImage.open(manager, id) }
            .recoverCatching { throwable: Throwable ->
                val parentSet: HTMaterialTextureSet = textureSet.parent ?: throw throwable
                getTextureResult(manager, parentSet, prefix).getOrThrow()
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
