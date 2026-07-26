package hiiragi283.core.api.color

import com.mojang.serialization.Codec
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.TextColor
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.util.FastColor
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item

/**
 * Minecraftで使用される様々な「色」をまとめたクラスです。
 *
 * 参考 : [Mekanism - EnumColor](https://github.com/mekanism/Mekanism/blob/1.21.x/src/api/java/mekanism/api/text/EnumColor.java)
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
enum class HTDefaultColor(
    val color: Int,
    val dyeColor: DyeColor,
    val textColor: TextColor,
    enName: String,
    jaName: String,
) : StringRepresentable,
    HTLangName by HTLangName(enName, jaName) {
    WHITE(ChatFormatting.WHITE, DyeColor.WHITE, "White", "白色"),
    ORANGE(intArrayOf(255, 161, 96), DyeColor.ORANGE, "Orange", "橙色"),
    MAGENTA(intArrayOf(213, 94, 203), DyeColor.MAGENTA, "Magenta", "赤紫色"),
    LIGHT_BLUE(ChatFormatting.AQUA, DyeColor.LIGHT_BLUE, "Light Blue", "空色"),
    YELLOW(ChatFormatting.YELLOW, DyeColor.YELLOW, "Yellow", "黄色"),
    LIME(ChatFormatting.GREEN, DyeColor.LIME, "Lime", "黄緑色"),
    PINK(ChatFormatting.LIGHT_PURPLE, DyeColor.PINK, "Pink", "桃色"),
    GRAY(ChatFormatting.DARK_GRAY, DyeColor.GRAY, "Gray", "灰色"),
    LIGHT_GRAY(ChatFormatting.GRAY, DyeColor.LIGHT_GRAY, "Light Gray", "薄灰色"),
    CYAN(ChatFormatting.DARK_AQUA, DyeColor.CYAN, "Cyan", "青緑色"),
    PURPLE(intArrayOf(164, 96, 217), DyeColor.PURPLE, "Purple", "紫色"),
    BLUE(ChatFormatting.BLUE, DyeColor.BLUE, "Blue", "青色"),
    BROWN(intArrayOf(161, 118, 73), DyeColor.BROWN, "Brown", "茶色"),
    GREEN(intArrayOf(89, 193, 95), DyeColor.GREEN, "Green", "緑色"),
    RED(ChatFormatting.RED, DyeColor.RED, "Red", "赤色"),
    BLACK(ChatFormatting.BLACK, DyeColor.BLACK, "Black", "黒色"),
    ;

    constructor(color: ChatFormatting, dyeColor: DyeColor, enName: String, jaName: String) : this(
        dyeColor.textureDiffuseColor,
        dyeColor,
        TextColor.fromLegacyFormat(color) ?: error("$color is not color format"),
        enName,
        jaName,
    )

    constructor(color: Int, dyeColor: DyeColor, enName: String, jaName: String) : this(
        color,
        dyeColor,
        TextColor.fromRgb(color),
        enName,
        jaName,
    )

    constructor(color: IntArray, dyeColor: DyeColor, enName: String, jaName: String) : this(
        FastColor.ARGB32.color(color[0], color[1], color[2]),
        dyeColor,
        enName,
        jaName,
    )

    companion object {
        @JvmField
        val CODEC: Codec<HTDefaultColor> = HTCodecs.stringEnum(HTDefaultColor::getSerializedName)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTDefaultColor> = HTStreamCodecs.enum()

        /**
         * @since 0.15.0
         */
        @JvmStatic
        fun fromDye(dyeColor: DyeColor): HTDefaultColor = entries.first { it.dyeColor == dyeColor }
    }

    /**
     * @since 0.10.0
     */
    val dyesTag: TagKey<Item> = dyeColor.tag

    /**
     * @since 0.10.0
     */
    val dyedTag: TagKey<Item> = dyeColor.dyedTag

    override fun getSerializedName(): String = name.lowercase()
}
