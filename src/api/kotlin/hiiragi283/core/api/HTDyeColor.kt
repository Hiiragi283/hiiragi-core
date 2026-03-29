package hiiragi283.core.api

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import java.awt.Color

/**
 * Minecraftで使用される様々な「色」をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 * @see mekanism.api.text.EnumColor
 */
enum class HTDyeColor(val dyeColor: DyeColor, enName: String, jaName: String) :
    StringRepresentable,
    HTLangName by HTLangName.create(enName, jaName) {
    WHITE(DyeColor.WHITE, "White", "白色"),
    ORANGE(DyeColor.ORANGE, "Orange", "橙色"),
    MAGENTA(DyeColor.MAGENTA, "Magenta", "赤紫色"),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE, "Light Blue", "空色"),
    YELLOW(DyeColor.YELLOW, "Yellow", "黄色"),
    LIME(DyeColor.LIME, "Lime", "黄緑色"),
    PINK(DyeColor.PINK, "Pink", "桃色"),
    GRAY(DyeColor.GRAY, "Gray", "灰色"),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY, "Light Gray", "薄灰色"),
    CYAN(DyeColor.CYAN, "Cyan", "青緑色"),
    PURPLE(DyeColor.PURPLE, "Purple", "紫色"),
    BLUE(DyeColor.BLUE, "Blue", "青色"),
    BROWN(DyeColor.BROWN, "Brown", "茶色"),
    GREEN(DyeColor.GREEN, "Green", "緑色"),
    RED(DyeColor.RED, "Red", "赤色"),
    BLACK(DyeColor.BLACK, "Black", "黒色"),
    ;

    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTDyeColor> = BiCodecs.stringEnum(HTDyeColor::getSerializedName)
    }

    val color: Int = dyeColor.textureDiffuseColor
    val colorObj = Color(this.color)

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
