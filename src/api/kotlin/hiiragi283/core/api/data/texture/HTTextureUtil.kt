package hiiragi283.core.api.data.texture

import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.resource.HTIdLike
import net.mehvahdjukaar.moonlight.api.resources.RPUtils
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.io.BufferedReader
import java.io.InputStream
import java.util.stream.Stream

/**
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
object HTTextureUtil {
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
    fun getCachedColors(id: HTIdLike): List<Int>? = getCachedColors(id.getId())

    @JvmStatic
    fun getCachedColors(id: ResourceLocation): List<Int>? = colorCache[id]

    @JvmStatic
    fun getOrCreateColors(id: ResourceLocation, manager: ResourceManager): Result<List<Int>> = getCachedColors(id)
        ?.let(Result.Companion::success)
        ?: runCatching { manager.getResource(id.withPath { "palettes/$it.gpl" }).get().open() }
            .mapCatching(InputStream::bufferedReader)
            .mapCatching(BufferedReader::lines)
            .map(Stream<String>::toList)
            .mapCatching { lines: List<String> ->
                val paletteId = "GIMP Palette"
                check(lines.firstOrNull() == paletteId) { "First line must be \"$paletteId\"" }
                lines
                    .filterNot { it == paletteId || it.startsWith("Name") || it.startsWith("Columns") }
                    .map { it.split(PALETTE_REGEX, limit = 4).take(3).map(String::toInt) }
                    .map { (red: Int, green: Int, blue: Int) -> RGBColor.combine(255, blue, green, red) }
            }.onSuccess { colorCache[id] = it }

    @JvmStatic
    fun getOrCreatePalette(id: ResourceLocation, manager: ResourceManager): Result<Palette> =
        getOrCreateColors(id, manager).map(::wrapToPalette)

    @JvmStatic
    fun wrapToPalette(colors: List<Int>): Palette = colors.map(::RGBColor).let(Palette::ofColors)

    //    TextureImage    //

    @JvmStatic
    fun getTexture(manager: ResourceManager, block: Block): Result<TextureImage> = runCatching {
        RPUtils.findFirstBlockTextureLocation(manager, block).let(TextureImage::open.partially1(manager))
    }

    @JvmStatic
    fun getTexture(manager: ResourceManager, item: Item): Result<TextureImage> = runCatching {
        RPUtils.findFirstItemTextureLocation(manager, item).let(TextureImage::open.partially1(manager))
    }
}
