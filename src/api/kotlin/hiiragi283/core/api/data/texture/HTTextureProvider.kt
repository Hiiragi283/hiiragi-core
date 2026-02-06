package hiiragi283.core.api.data.texture

import com.google.common.hash.HashCode
import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import net.minecraft.Util
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.awt.Color
import java.io.IOException
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

/**
 * テクスチャを生成する[DataProvider]の抽象クラスです。
 */
abstract class HTTextureProvider(protected val modId: String, packOutput: PackOutput, private val fileHelper: ExistingFileHelper) :
    DataProvider {
    constructor(modid: String, context: HTDataGenContext) : this(modid, context.output, context.fileHelper)

    private val pathProvider: PackOutput.PathProvider =
        packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, HTConst.TEXTURES)

    override fun run(output: CachedOutput): CompletableFuture<*> {
        HTTextureUtil.TEMPLATE_PALETTE.forEachIndexed { index: Int, color: Color ->
            DataProvider.LOGGER.info("Template color at {} is {}", index, color.rgb)
        }

        val set: MutableSet<ResourceLocation> = mutableSetOf()
        val list: MutableList<CompletableFuture<*>> = mutableListOf()
        gather { id: ResourceLocation, image: NativeImage ->
            check(set.add(id)) { "Duplicate texture $id" }
            list += writeImage(output, image, pathProvider.file(id, "png"))
            fileHelper.trackGenerated(id, ModelProvider.TEXTURE)
        }
        return CompletableFuture.allOf(*list.toTypedArray())
    }

    /**
     * 生成するテクスチャを集めます。
     * @param output 生成するテクスチャとそのIDの出力先
     */
    protected abstract fun gather(output: BiConsumer<ResourceLocation, NativeImage>)

    private fun writeImage(output: CachedOutput, image: NativeImage, path: Path): CompletableFuture<*> = CompletableFuture.runAsync(
        {
            try {
                val byteArray: ByteArray = image.asByteArray()
                output.writeIfNeeded(path, byteArray, HashCode.fromBytes(byteArray))
            } catch (exception: IOException) {
                DataProvider.LOGGER.error("Failed to save image to {}", path, exception)
            }
        },
        Util.backgroundExecutor(),
    )

    override fun getName(): String = "Texture"

    //    Extensions    //

    /**
     * 素材ブロックとアイテムのテクスチャを生成します。
     * @since 0.8.0
     */
    protected fun material(output: BiConsumer<ResourceLocation, NativeImage>) {
        with(HiiragiCoreAccess.INSTANCE.materialContents) {
            material(output, HTConst.BLOCK, ::getBlockMap)
            material(output, HTConst.ITEM, ::getItemMap)
        }
    }

    protected fun material(
        output: BiConsumer<ResourceLocation, NativeImage>,
        pathPrefix: String,
        factory: (HTMaterialLike) -> Map<HTTagPrefix, HTIdLike>,
    ) {
        // すべての素材に対してテクスチャの生成を試みる
        for (entry: HTMaterialManager.Entry in HiiragiCoreAccess.INSTANCE.materialManager) {
            // 素材の名前空間がmodIdと異なる場合はパス
            if (entry.namespace != modId) continue
            // 生成対象がない場合はパス
            val prefixedMap: Map<HTTagPrefix, HTIdLike> = factory(entry)
            if (prefixedMap.isEmpty()) continue
            // テクスチャを生成
            val textureSet: HTMaterialTextureSet = entry.getOrDefault(HTMaterialPropertyKeys.TEXTURE_SET)
            for ((prefix: HTTagPrefix, element: HTIdLike) in prefixedMap) {
                if (HTTagPropertyKeys.DISABLE_TEXTURE_GEN in prefix) continue
                // パレットを取得
                val palette: List<Color> = sequence {
                    if (HTTagPropertyKeys.IS_RAW in prefix) {
                        yield(entry[HTMaterialPropertyKeys.TEXTURE_COLOR_RAW] ?: entry.getId().withPrefix("raw_"))
                    }
                    yield(entry[HTMaterialPropertyKeys.TEXTURE_COLOR] ?: entry.getId())
                }.map(HTTextureUtil::getPalette)
                    .firstNotNullOfOrNull { it.getOrNull() }
                    ?: continue
                // テンプレートを取得
                val template: NativeImage = getTextureResult(textureSet, prefix).getOrNull() ?: continue
                copyAndApplyColor(
                    output,
                    element.getId().withPrefix("$pathPrefix/"),
                    palette,
                    template,
                )
            }
        }
    }

    protected fun getTextureResult(textureSet: HTMaterialTextureSet, prefix: HTTagPrefix): Result<NativeImage> = HiiragiCoreAPI
        .id("material_set", textureSet.name, prefix[HTTagPropertyKeys.TEXTURE_ICON] ?: prefix.name)
        .let(HTTextureUtil::getTexture)
        .recoverCatching { throwable: Throwable ->
            val parentSet: HTMaterialTextureSet = textureSet.parent ?: throw throwable
            getTextureResult(parentSet, prefix).getOrThrow()
        }

    /**
     * 素材ツールのテクスチャを生成します。
     * @since 0.9.0
     */
    protected fun tool(output: BiConsumer<ResourceLocation, NativeImage>) {
        // すべての素材に対してテクスチャの生成を試みる
        for (entry: HTMaterialManager.Entry in HiiragiCoreAccess.INSTANCE.materialManager) {
            // 素材の名前空間がmodIdと異なる場合はパス
            if (entry.namespace != modId) continue
            // 生成対象がない場合はパス
            val toolMap: Map<HTToolType, HTIdLike> = HiiragiCoreAccess.INSTANCE.materialContents.getToolMap(entry)
            if (toolMap.isEmpty()) continue
            // パレットを取得
            val paletteId: ResourceLocation = (entry[HTMaterialPropertyKeys.TEXTURE_COLOR] ?: entry.getId())
            val palette: List<Color> = HTTextureUtil.getPalette(paletteId).getOrNull() ?: continue
            // テクスチャを生成
            for ((toolType: HTToolType, item: HTIdLike) in toolMap) {
                // テンプレートを取得
                val templateId: ResourceLocation = HiiragiCoreAPI.id("tool_set", toolType.name)
                val template: NativeImage = HTTextureUtil.getTexture(templateId).getOrNull() ?: continue
                copyAndApplyColor(output, item.itemId, palette, template)
            }
        }
    }

    protected fun copyAndApplyColor(
        output: BiConsumer<ResourceLocation, NativeImage>,
        id: ResourceLocation,
        paletteId: ResourceLocation,
        templateId: ResourceLocation,
    ) {
        val palette: List<Color> = HTTextureUtil.getPalette(paletteId).getOrNull() ?: return
        val template: NativeImage = HTTextureUtil.getTexture(templateId).getOrNull() ?: return
        copyAndApplyColor(output, id, palette, template)
    }

    protected fun copyAndApplyColor(
        output: BiConsumer<ResourceLocation, NativeImage>,
        id: ResourceLocation,
        palette: List<Color>,
        template: NativeImage,
    ) {
        val image: NativeImage = HTTextureUtil.copyFrom(template)
        for ((index: Int, pixels: Set<Pair<Int, Int>>) in createTemplate(template)) {
            for ((x: Int, y: Int) in pixels) {
                image.setPixelRGBA(x, y, HTTextureUtil.argbToFromABGR(palette[index].rgb))
            }
        }
        output.accept(id, image)
    }

    protected fun createTemplate(image: NativeImage): Map<Int, Set<Pair<Int, Int>>> = buildMap {
        for (x: Int in (0..<image.width)) {
            for (y: Int in (0..<image.height)) {
                val color: Color = image.getPixelRGBA(x, y).let(HTTextureUtil::argbToFromABGR).let(::Color)
                val index: Int = HTTextureUtil.TEMPLATE_PALETTE.indexOf(color)
                if (index >= 0) {
                    this[index] = (this[index]?.plus(x to y) ?: setOf(x to y))
                }
            }
        }
    }
}
