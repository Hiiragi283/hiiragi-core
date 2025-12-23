package hiiragi283.core.api.data.texture

import com.google.common.hash.HashCode
import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.ImmutableMultiMap
import hiiragi283.core.api.collection.buildMultiMap
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
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
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator
import kotlin.io.path.inputStream

/**
 * テクスチャを生成する[DataProvider]の抽象クラスです。
 */
abstract class HTTextureProvider(private val packOutput: PackOutput, private val fileHelper: ExistingFileHelper) : DataProvider {
    constructor(context: HTDataGenContext) : this(context.output, context.fileHelper)

    private val pathProvider: PackOutput.PathProvider =
        packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "textures")

    override fun run(output: CachedOutput): CompletableFuture<*> {
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
     * 指定した[id]から既存のテクスチャを取得します。
     * @return [id]からテクスチャを取得できない場合は`null`
     */
    protected fun getTexture(id: ResourceLocation): NativeImage? {
        val filePath: Path = packOutput.outputFolder.resolve("../../main/resources/assets/${id.namespace}/textures/${id.path}.png")
        try {
            return NativeImage.read(filePath.inputStream())
        } catch (_: IOException) {
            DataProvider.LOGGER.error("Failed to load image to {}", filePath)
        }
        return null
    }

    /**
     * 指定した[テクスチャ][other]をコピーします。
     * @return コピーされたテクスチャ
     */
    protected fun copyFrom(other: NativeImage): NativeImage {
        val image = NativeImage(other.width, other.height, true)
        image.copyFrom(other)
        return image
    }
    
    protected fun <T : HTMaterialLike> material(
        output: BiConsumer<ResourceLocation, NativeImage>,
        pathPrefix: String,
        materials: Iterable<T>,
        supportedPrefixes: (T) -> Set<HTMaterialPrefix>,
        templateFactory: (T, HTMaterialPrefix) -> ResourceLocation?,
        paletteGetter: (T) -> HTColorPalette,
    ) {
        for (material: T in materials) {
            for (prefix: HTMaterialPrefix in supportedPrefixes(material)) {
                val templateImage: NativeImage = templateFactory(material, prefix)?.let(::getTexture) ?: continue
                val image: NativeImage = copyFrom(templateImage)

                for ((index: Int, pixels: Collection<Pair<Int, Int>>) in createTemplate(templateImage).map) {
                    for ((x: Int, y: Int) in pixels) {
                        image.setPixelRGBA(x, y, argbToFromABGR(paletteGetter(material)[index].rgb))
                    }
                }
                output.accept(HiiragiCoreAPI.id(pathPrefix, prefix.asPrefixName(), material.asMaterialName()), image)
            }
        }
    }

    protected fun createTemplate(image: NativeImage): ImmutableMultiMap<Int, Pair<Int, Int>> = buildMultiMap {
        for (x: Int in (0..<image.width)) {
            for (y: Int in (0..<image.height)) {
                val color = Color(argbToFromABGR(image.getPixelRGBA(x, y)))
                val index: Int = getColorIndex(color) ?: continue
                put(index, x to y)
            }
        }
    }

    private fun getColorIndex(color: Color): Int? = when (color) {
        Color(0xd8d8d8) -> 0
        Color(0xb2b2b2) -> 1
        Color(0x8b8b8b) -> 2
        Color(0x656565) -> 3
        Color(0x3e3e3e) -> 4
        Color(0x181818) -> 5
        else -> null
    }

    /**
     * @see mekanism.common.lib.Color.argbToFromABGR
     */
    protected fun argbToFromABGR(argb: Int): Int {
        val red: Int = argb shr 16 and 0xFF
        val blue: Int = argb and 0xFF
        return argb and -0xff0100 or (blue shl 16) or red
    }
}
