package hiiragi283.core.api.data.texture

import hiiragi283.core.api.function.partially1
import net.mehvahdjukaar.moonlight.api.resources.RPUtils
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor
import net.minecraft.data.DataProvider
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
    fun getPalette(manager: ResourceManager, id: ResourceLocation): Result<List<Int>> =
        runCatching { manager.getResource(id.withPath { "palettes/$it.gpl" }).get().open() }
            .mapCatching(InputStream::bufferedReader)
            .mapCatching(BufferedReader::lines)
            .map(Stream<String>::toList)
            .mapCatching { lines: List<String> ->
                check(lines.firstOrNull() == "GIMP Palette")
                lines
                    .filterNot { it == "GIMP Palette" || it.startsWith("Name") || it.startsWith("Columns") }
                    .map { it.split(PALETTE_REGEX, limit = 4).take(3).map(String::toInt) }
                    .map { (red: Int, green: Int, blue: Int) -> RGBColor.combine(255, blue, green, red) }
            }.onFailure { DataProvider.LOGGER.warn("Failed to load palette: $id") }

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
