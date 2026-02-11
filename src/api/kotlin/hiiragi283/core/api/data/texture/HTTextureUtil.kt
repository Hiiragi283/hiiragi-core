package hiiragi283.core.api.data.texture

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.partially1
import net.mehvahdjukaar.moonlight.api.resources.RPUtils
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor
import net.minecraft.data.DataProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.fml.ModList
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
    val TEMPLATE_PALETTE: Palette by lazy { getPalette(HiiragiCoreAPI.id("template")).getOrThrow() }

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
    private val colorCache: MutableMap<ResourceLocation, Palette> = hashMapOf()

    @JvmStatic
    fun getPalette(id: ResourceLocation): Result<Palette> = colorCache[id]
        ?.let(Result.Companion::success)
        ?: runCatching(getResourcePath(id, "palettes", "gpl")::inputStream)
            .mapCatching(InputStream::bufferedReader)
            .mapCatching(BufferedReader::lines)
            .map(Stream<String>::toList)
            .mapCatching { lines: List<String> ->
                check(lines.firstOrNull() == "GIMP Palette")
                lines
                    .filterNot { it == "GIMP Palette" || it.startsWith("Name") || it.startsWith("Columns") }
                    .map { it.split(PALETTE_REGEX, limit = 4).take(3).map(String::toInt) }
                    .map { (red: Int, green: Int, blue: Int) -> RGBColor.combine(255, blue, green, red).let(::RGBColor) }
                    .let(Palette::ofColors)
            }.onSuccess { colorCache[id] = it }
            .onFailure { DataProvider.LOGGER.warn("Failed to load palette: $id") }

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
