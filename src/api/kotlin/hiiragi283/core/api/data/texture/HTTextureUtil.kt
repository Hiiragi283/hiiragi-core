package hiiragi283.core.api.data.texture

import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.data.DataProvider
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.ModList
import java.awt.Color
import java.io.BufferedReader
import java.io.InputStream
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.io.path.inputStream

/**
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
object HTTextureUtil {
    @JvmStatic
    private val PALETTE_REGEX = Regex("\\s+")

    @JvmStatic
    val TEMPLATE_PALETTE: List<Color> = getPalette(HiiragiCoreAPI.id("template")).getOrThrow()

    @JvmStatic
    private fun getResourcePath(id: ResourceLocation, prefix: String, extension: String): Path = ModList
        .get()
        .getModFileById(id.namespace)
        .file
        .secureJar
        .rootPath
        .resolve("assets/${id.namespace}/$prefix/${id.path}.$extension")

    //    Color    //

    @JvmStatic
    fun getPalette(id: ResourceLocation): Result<List<Color>> = runCatching(getResourcePath(id, "palettes", "gpl")::inputStream)
        .mapCatching(InputStream::bufferedReader)
        .mapCatching(BufferedReader::lines)
        .map(Stream<String>::toList)
        .mapCatching { lines: List<String> ->
            check(lines.firstOrNull() == "GIMP Palette")
            lines
                .filterNot { it == "GIMP Palette" || it.startsWith("Name") || it.startsWith("Columns") }
                .map { it.split(PALETTE_REGEX, limit = 4).take(3).map(String::toInt) }
                .map { (red: Int, green: Int, blue: Int) -> Color(red, green, blue) }
        }.onFailure { DataProvider.LOGGER.warn("Failed to load palette: $id") }

    /**
     * @see mekanism.common.lib.Color.argbToFromABGR
     */
    @JvmStatic
    fun argbToFromABGR(argb: Int): Int {
        val red: Int = argb shr 16 and 0xFF
        val blue: Int = argb and 0xFF
        return argb and -0xff0100 or (blue shl 16) or red
    }

    //    Image    //

    /**
     * 指定した[id]から既存のテクスチャを取得します。
     */
    @JvmStatic
    fun getTexture(id: ResourceLocation): Result<NativeImage> = runCatching(getResourcePath(id, "textures", "png")::inputStream)
        .mapCatching(NativeImage::read)
        .onFailure { DataProvider.LOGGER.warn("Failed to load image: $id") }

    /**
     * 指定した[テクスチャ][other]をコピーします。
     * @return コピーされたテクスチャ
     */
    @JvmStatic
    fun copyFrom(other: NativeImage): NativeImage {
        val image = NativeImage(other.width, other.height, true)
        image.copyFrom(other)
        return image
    }

    @JvmStatic
    fun merge(base: NativeImage, overlay: NativeImage, replace: Boolean) {
        check(base.width == overlay.width) { "Require same width" }
        check(base.height == overlay.height) { "Require same height" }
        for (x: Int in (0..<base.width)) {
            for (y: Int in (0..<base.height)) {
                val baseColor: Color = base.getPixelRGBA(x, y).let(::argbToFromABGR).let(::Color)
                if (baseColor.alpha == 0 || replace) {
                    base.setPixelRGBA(x, y, overlay.getPixelRGBA(x, y))
                }
            }
        }
    }
}
