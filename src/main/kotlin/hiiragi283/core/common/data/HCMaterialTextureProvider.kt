package hiiragi283.core.common.data

import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.pack.HTDynamicResourceRegister
import hiiragi283.core.api.data.texture.HTTextureUtil
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import kotlin.collections.iterator
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager

data object HCMaterialTextureProvider {
    @JvmStatic
    fun reload(manager: ResourceManager) {
        HTTextureUtil.templatePalette = HTTextureUtil.getOrCreateColors(HiiragiCoreAPI.id("template"), manager).getOrThrow()

        val contents: HTMaterialAccess = HiiragiCoreAccess.INSTANCE.registeredContents
        material(manager, HTConst.BLOCK, contents.blocks::column)
        material(manager, HTConst.ITEM, contents.items::column)
        tool(manager)
        // molten(manager)
    }

    @JvmStatic
    private inline fun <T : HTIdLike> material(manager: ResourceManager, pathPrefix: String, factory: (HTMaterialKey) -> Map<HTPart, T>) {
        // すべての素材に対してテクスチャの生成を試みる
        for (material: HTMaterial in HTMaterialManager.getInstance()) {
            val key: HTMaterialKey = material.key
            // 生成対象がない場合はパス
            val partMap: Map<HTPart, T> = factory(key)
            if (partMap.isEmpty()) continue
            // テクスチャを生成
            val textureSet: HTMaterialTextureSet = material.getOrDefault(HTMaterialPropertyKeys.TEXTURE_SET)
            for (part: HTPart in partMap.keys) {
                if (HTPartPropertyKeys.DISABLE_TEXTURE_GEN in part) continue
                // パレットを取得
                val palette: List<Int> = sequence {
                    if (HTPartPropertyKeys.IS_RAW in part) {
                        yield(material[HTMaterialPropertyKeys.TEXTURE_COLOR_RAW] ?: key.getId().withPrefix("raw_"))
                    }
                    yield(material[HTMaterialPropertyKeys.TEXTURE_COLOR] ?: key.getId())
                }.firstNotNullOfOrNull { HTTextureUtil.getOrCreateColors(it, manager).getOrNull() }
                    ?: run {
                        missingPalette(key)
                        continue
                    }
                // テンプレートを取得
                val template: NativeImage = getTextureResult(manager, textureSet, part)
                    .onFailure { HiiragiCoreAPI.LOGGER.error("Failed to get template image for part ${part.key}") }
                    .getOrNull()
                    ?: continue
                copyAndApplyColor(part.createId(key).withPrefix("$pathPrefix/"), palette, template)
            }
        }
    }

    @JvmStatic
    private fun tool(manager: ResourceManager) {
        // すべての素材に対してテクスチャの生成を試みる
        for (material: HTMaterial in HTMaterialManager.getInstance()) {
            val key: HTMaterialKey = material.key
            if (HTMaterialPropertyKeys.TOOL_MATERIAL !in material) continue
            val toolMap: Map<HTToolType, HTIdLike> = HiiragiCoreAccess.INSTANCE.registeredContents.tools
                .column(key)
            if (toolMap.isEmpty()) continue
            // パレットを取得
            val palette: List<Int> = (material[HTMaterialPropertyKeys.TEXTURE_COLOR] ?: key.getId())
                .let { HTTextureUtil.getOrCreateColors(it, manager).getOrNull() }
                ?: run {
                    missingPalette(key)
                    continue
                }
            // テンプレートを取得
            for ((toolType: HTToolType, item: HTIdLike) in toolMap) {
                val toolTypeName: String = toolType.name
                val textureId: ResourceLocation = HiiragiCoreAPI.id("textures", "tool_set", "$toolTypeName.png")
                val template: NativeImage = HTTextureUtil.openImage(manager, textureId)
                    .onFailure { HiiragiCoreAPI.LOGGER.error("Failed to get template image for tool type $toolTypeName") }
                    .getOrNull()
                    ?: continue
                copyAndApplyColor(item.itemId, palette, template)
            }
        }
    }

    @JvmStatic
    private fun missingPalette(key: HTMaterialKey) {
        HiiragiCoreAPI.LOGGER.error("Failed to get color palette for material; $key")
    }

    /*private fun molten(manager: ResourceManager, sink: ResourceSink) {
        // すべての素材に対してテクスチャの生成を試みる
        for ((key: HTMaterialKey, entry: HTPropertyGetter) in HTMaterialManager.getInstance()) {
            val molten: HTIdLike = HiiragiCoreAccess.INSTANCE.registeredFluids[HTFluidPart.MOLTEN, key] ?: continue
            // パレットを取得
            var palette: Palette = HTTextureUtil
                .getOrCreatePalette(entry[HTMaterialPropertyKeys.TEXTURE_COLOR] ?: key.toId(HiiragiCoreAPI.MOD_ID), manager)
                .getOrNull()
                ?: continue
            palette = Palette.fromArc(palette.elementAt(1).lab(), palette.elementAt(palette.size - 1).lab(), 9)
            HiiragiCoreAPI.LOGGER.debug(
                "Molten {} palette: {}",
                key,
                palette.joinToString(separator = ",", transform = { it.value().toHexString() }),
            )
            // テンプレートを取得
            val template: TextureImage = lavaTexture.makeCopy()
            val respriter: Respriter = Respriter.of(template)
            val newImage: TextureImage = respriter.recolor(palette)
            sink.addTexture(molten.blockId, newImage)
        }
    }*/

    @JvmStatic
    private fun getTextureResult(manager: ResourceManager, textureSet: HTMaterialTextureSet, part: HTPart): Result<NativeImage> = HTTextureUtil.openImage(manager, HiiragiCoreAPI.id("textures", "material_set", textureSet.name, "${part.asPartName()}.png"))
        .recoverCatching { throwable: Throwable ->
            val parentSet: HTMaterialTextureSet = textureSet.parent ?: throw throwable
            getTextureResult(manager, parentSet, part).getOrThrow()
        }

    @JvmStatic
    private fun copyAndApplyColor(id: ResourceLocation, palette: List<Int>, template: NativeImage) {
        val image: NativeImage = HTTextureUtil.copyFrom(template)
        for ((index: Int, pixels: Set<Pair<Int, Int>>) in createTemplate(template)) {
            for ((x: Int, y: Int) in pixels) {
                image.setPixelRGBA(x, y, palette[index])
            }
        }
        HTDynamicResourceRegister.addTexture(id, image)
    }

    @JvmStatic
    private fun createTemplate(image: NativeImage): Map<Int, Set<Pair<Int, Int>>> = buildMap {
        for (x: Int in (0..<image.width)) {
            for (y: Int in (0..<image.height)) {
                val index: Int = HTTextureUtil.templatePalette.indexOf(image.getPixelRGBA(x, y))
                if (index >= 0) {
                    this[index] = (this[index]?.plus(x to y) ?: setOf(x to y))
                }
            }
        }
    }
}
