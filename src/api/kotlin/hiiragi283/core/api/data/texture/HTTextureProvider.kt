package hiiragi283.core.api.data.texture

import com.google.common.hash.HashCode
import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
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
        packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "textures")

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
     * @since 0.8.0
     */
    protected fun material(output: BiConsumer<ResourceLocation, NativeImage>) {
        material(output, HTConst.BLOCK, HTMaterialContentsAccess.INSTANCE.getBlockTable())
        material(output, HTConst.ITEM, HTMaterialContentsAccess.INSTANCE.getItemTable())
    }

    protected fun material(
        output: BiConsumer<ResourceLocation, NativeImage>,
        pathPrefix: String,
        table: HTTable<HTTagPrefix, HTMaterialKey, *>,
    ) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in HTMaterialManager.INSTANCE) {
            if (key.getNamespace() != modId) continue
            val paletteId: ResourceLocation = (propertyMap[HTMaterialPropertyKeys.TEXTURE_COLOR] ?: key.getId())
            val colorPalette: List<Color> = paletteId
                .let(HTTextureUtil::getPalette)
                .onFailure { DataProvider.LOGGER.warn("Failed to load palette: $paletteId") }
                .getOrNull()
                ?: continue

            val textureSet: HTMaterialTextureSet = propertyMap.getOrDefault(HTMaterialPropertyKeys.TEXTURE_SET)
            for (prefix: HTTagPrefix in table.column(key).keys) {
                val templateImage: NativeImage = getTexture(textureSet, prefix) ?: continue
                val image: NativeImage = HTTextureUtil.copyFrom(templateImage)

                for ((index: Int, pixels: Set<Pair<Int, Int>>) in createTemplate(templateImage)) {
                    for ((x: Int, y: Int) in pixels) {
                        image.setPixelRGBA(x, y, HTTextureUtil.argbToFromABGR(colorPalette[index].rgb))
                    }
                }
                output.accept(prefix.createId(key).withPrefix("$pathPrefix/"), image)
            }
        }
    }

    protected fun getTexture(textureSet: HTMaterialTextureSet, prefix: HTTagPrefix): NativeImage? = getTextureResult(textureSet, prefix)
        .onFailure { DataProvider.LOGGER.error("Failed to load image", it) }
        .getOrNull()

    protected fun getTextureResult(textureSet: HTMaterialTextureSet, prefix: HTTagPrefix): Result<NativeImage> = HiiragiCoreAPI
        .id("material_set", textureSet.name, prefix[HTTagPropertyKeys.TEXTURE_ICON] ?: prefix.name)
        .let(HTTextureUtil::getTexture)
        .recoverCatching { throwable: Throwable ->
            val parentSet: HTMaterialTextureSet = textureSet.parent ?: throw throwable
            getTextureResult(parentSet, prefix).getOrThrow()
        }

    protected fun createTemplate(image: NativeImage): Map<Int, Set<Pair<Int, Int>>> = buildMap {
        for (x: Int in (0..<image.width)) {
            for (y: Int in (0..<image.height)) {
                val color = Color(HTTextureUtil.argbToFromABGR(image.getPixelRGBA(x, y)))
                val index: Int = HTTextureUtil.TEMPLATE_PALETTE.indexOf(color)
                if (index >= 0) {
                    this[index] = (this[index]?.plus(x to y) ?: setOf(x to y))
                }
            }
        }
    }
}
