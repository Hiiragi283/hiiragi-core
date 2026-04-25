package hiiragi283.core.api

import com.mojang.serialization.Codec
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.TextColor
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.util.FastColor
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item

/**
 * Minecraftで使用される様々な「色」をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 * @see mekanism.api.text.EnumColor
 */
enum class HTDefaultColor(
    val color: Int,
    val dyeColor: DyeColor,
    val textColor: TextColor,
    enName: String,
    jaName: String,
) : StringRepresentable,
    HTLangName by HTLangName.create(enName, jaName),
    HTMaterialLike {
    WHITE(intArrayOf(255, 255, 255), DyeColor.WHITE, "White", "白色"),
    ORANGE(intArrayOf(255, 161, 96), DyeColor.ORANGE, "Orange", "橙色"),
    MAGENTA(intArrayOf(213, 94, 203), DyeColor.MAGENTA, "Magenta", "赤紫色"),
    LIGHT_BLUE(intArrayOf(85, 158, 255), DyeColor.LIGHT_BLUE, "Light Blue", "空色"),
    YELLOW(intArrayOf(255, 221, 79), DyeColor.YELLOW, "Yellow", "黄色"),
    LIME(intArrayOf(117, 255, 137), DyeColor.LIME, "Lime", "黄緑色"),
    PINK(intArrayOf(255, 188, 196), DyeColor.PINK, "Pink", "桃色"),
    GRAY(intArrayOf(122, 122, 122), DyeColor.GRAY, "Gray", "灰色"),
    LIGHT_GRAY(intArrayOf(207, 207, 207), DyeColor.LIGHT_GRAY, "Light Gray", "薄灰色"),
    CYAN(intArrayOf(0, 243, 208), DyeColor.CYAN, "Cyan", "青緑色"),
    PURPLE(intArrayOf(164, 96, 217), DyeColor.PURPLE, "Purple", "紫色"),
    BLUE(intArrayOf(54, 107, 208), DyeColor.BLUE, "Blue", "青色"),
    BROWN(intArrayOf(161, 118, 73), DyeColor.BROWN, "Brown", "茶色"),
    GREEN(intArrayOf(89, 193, 95), DyeColor.GREEN, "Green", "緑色"),
    RED(intArrayOf(255, 56, 60), DyeColor.RED, "Red", "赤色"),
    BLACK(intArrayOf(64, 64, 64), DyeColor.BLACK, "Black", "黒色"),
    ;

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

    override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(HiiragiCoreAPI.id(serializedName))
}
