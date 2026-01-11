package hiiragi283.core.api

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.TextColor
import net.minecraft.util.FastColor
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.material.MapColor

/**
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 * @see mekanism.api.text.EnumColor
 */
enum class HTDefaultColor(
    val color: Int,
    val dyeColor: DyeColor,
    val textColor: TextColor,
    val mapColor: MapColor = dyeColor.mapColor,
) : StringRepresentable,
    HTMaterialLike {
    WHITE(intArrayOf(255, 255, 255), DyeColor.WHITE),
    ORANGE(intArrayOf(255, 161, 96), DyeColor.ORANGE),
    MAGENTA(intArrayOf(213, 94, 203), DyeColor.MAGENTA),
    LIGHT_BLUE(intArrayOf(85, 158, 255), DyeColor.LIGHT_BLUE),
    YELLOW(intArrayOf(255, 221, 79), DyeColor.YELLOW),
    LIME(intArrayOf(117, 255, 137), DyeColor.LIME),
    PINK(intArrayOf(255, 188, 196), DyeColor.PINK),
    GRAY(intArrayOf(122, 122, 122), DyeColor.GRAY),
    LIGHT_GRAY(intArrayOf(207, 207, 207), DyeColor.LIGHT_GRAY),
    CYAN(intArrayOf(0, 243, 208), DyeColor.CYAN),
    PURPLE(intArrayOf(164, 96, 217), DyeColor.PURPLE),
    BLUE(intArrayOf(54, 107, 208), DyeColor.BLUE),
    BROWN(intArrayOf(161, 118, 73), DyeColor.BROWN),
    GREEN(intArrayOf(89, 193, 95), DyeColor.GREEN),
    RED(intArrayOf(255, 56, 60), DyeColor.RED),
    BLACK(intArrayOf(64, 64, 64), DyeColor.BLACK),
    ;

    constructor(color: Int, dyeColor: DyeColor) : this(
        color,
        dyeColor,
        TextColor.fromRgb(color),
    )

    constructor(color: IntArray, dyeColor: DyeColor) : this(
        FastColor.ARGB32.color(color[0], color[1], color[2]),
        dyeColor,
    )

    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTDefaultColor> = BiCodecs.stringEnum(HTDefaultColor::getSerializedName)
    }

    override fun getSerializedName(): String = name.lowercase()

    override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(serializedName)
}
