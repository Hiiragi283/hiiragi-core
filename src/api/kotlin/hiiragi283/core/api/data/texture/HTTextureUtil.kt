package hiiragi283.core.api.data.texture

import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.resource.modifyPath
import java.io.BufferedReader
import java.io.InputStream
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
        ?: runCatching {
            manager.open(id.modifyPath { "palettes/$it.gpl" })
                .use(InputStream::bufferedReader)
                .use(BufferedReader::lines)
                .toList()
                .let { lines: List<String> ->
                    val paletteId = "GIMP Palette"
                    check(lines.firstOrNull() == paletteId) { "First line must be \"$paletteId\"" }
                    lines
                        .filterNot { it == paletteId || it.startsWith("Name") || it.startsWith("Columns") }
                        .map { it.split(PALETTE_REGEX, limit = 4).take(3).map(String::toInt) }
                        .map { (red: Int, green: Int, blue: Int) -> combine(255, blue, green, red) }
                }
        }.onSuccess { colorCache[id] = it }

    /**
     * @since 21.1.1.0
     */
    @JvmStatic
    fun combine(alpha: Int, blue: Int, green: Int, red: Int): Int = (alpha shl 24) or (blue shl 16) or (green shl 8) or red

    //    NativeImage    //

    /**
     * テクスチャを[NativeImage]として取得します。
     * @param manager テクスチャの提供元
     * @param id テクスチャのパス（末尾に`.png`を含むこと）
     * @since 21.1.1.0
     */
    @JvmStatic
    fun openImage(manager: ResourceManager, id: ResourceLocation): Result<NativeImage> = runCatching { manager.open(id).use(NativeImage::read) }

    /**
     * 指定した画像をコピーします。
     * @return 新しい画像のインスタンス
     * @since 21.1.1.0
     */
    @JvmStatic
    fun copyFrom(other: NativeImage): NativeImage {
        val image = NativeImage(other.width, other.height, true)
        image.copyFrom(other)
        return image
    }
}
