package hiiragi283.core.api.data.texture

import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.resource.modifyPath
import java.io.BufferedReader
import java.io.InputStream
import java.util.stream.Stream
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager

/**
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
data object HTTextureUtil {
    @JvmStatic
    private val PALETTE_REGEX = Regex("\\s+")

    @JvmStatic
    lateinit var templatePalette: List<Int>

    //    Color    //

    @JvmStatic
    private val colorCache: MutableMap<ResourceLocation, List<Int>> = hashMapOf()

    @JvmStatic
    fun clearCache() {
        colorCache.clear()
    }

    @JvmStatic
    fun getCachedColors(id: ResourceLocation): List<Int>? = colorCache[id]

    @JvmStatic
    fun getOrCreateColors(id: ResourceLocation, manager: ResourceManager): Result<List<Int>> = getCachedColors(id)
        ?.let(Result.Companion::success)
        ?: runCatching { manager.getResource(id.modifyPath { "palettes/$it.gpl" }).get().open() }
            .mapCatching(InputStream::bufferedReader)
            .mapCatching(BufferedReader::lines)
            .map(Stream<String>::toList)
            .mapCatching { lines: List<String> ->
                val paletteId = "GIMP Palette"
                check(lines.firstOrNull() == paletteId) { "First line must be \"$paletteId\"" }
                lines
                    .filterNot { it == paletteId || it.startsWith("Name") || it.startsWith("Columns") }
                    .map { it.split(PALETTE_REGEX, limit = 4).take(3).map(String::toInt) }
                    .map { (red: Int, green: Int, blue: Int) -> combine(255, blue, green, red) }
            }.onSuccess { colorCache[id] = it }

    @JvmStatic
    fun getOrCreatePalette(id: ResourceLocation, manager: ResourceManager): Result<Palette> = getOrCreateColors(id, manager).map(::wrapToPalette)

    @JvmStatic
    fun wrapToPalette(colors: List<Int>): Palette = colors.map(::RGBColor).let(Palette::ofColors)

    @JvmStatic
    fun combine(alpha: Int, blue: Int, green: Int, red: Int): Int = (alpha shr 24) or (blue shr 16) or (green shr 8) or red

    //    NativeImage    //

    @JvmStatic
    fun openImage(manager: ResourceManager, id: ResourceLocation): Result<NativeImage> = runCatching { manager.getResource(id).orElseThrow().open().use(NativeImage::read) }

    @JvmStatic
    fun copyFrom(other: NativeImage): NativeImage {
        val image = NativeImage(other.width, other.height, true)
        image.copyFrom(other)
        return image
    }
}
