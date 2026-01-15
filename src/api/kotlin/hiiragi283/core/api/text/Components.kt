package hiiragi283.core.api.text

import hiiragi283.core.api.HTDefaultColor
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.extensions.ILevelExtension

//    Text    //

/**
 * 指定した[文字列][this]を[テキスト][MutableComponent]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun String.toText(): MutableComponent = Component.literal(this)

/**
 * 指定した[文字列][value]を翻訳された[テキスト][MutableComponent]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun translatableText(value: String): MutableComponent = Component.translatable(value)

/**
 * 指定した[文字列][value]と[引数][args]を翻訳された[テキスト][MutableComponent]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun translatableText(value: String, vararg args: Any): MutableComponent = Component.translatable(value, *args)

/**
 * 指定した[Boolean]を翻訳された[テキスト][MutableComponent]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun boolText(value: Boolean): MutableComponent = when (value) {
    true -> HTCommonTranslation.TRUE
    false -> HTCommonTranslation.FALSE
}.translate()

/**
 * 指定した[Direction]を翻訳された[テキスト][MutableComponent]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
fun directionText(direction: Direction): MutableComponent = when (direction) {
    Direction.DOWN -> HTCommonTranslation.DOWN
    Direction.UP -> HTCommonTranslation.UP
    Direction.NORTH -> HTCommonTranslation.NORTH
    Direction.SOUTH -> HTCommonTranslation.SOUTH
    Direction.WEST -> HTCommonTranslation.WEST
    Direction.EAST -> HTCommonTranslation.EAST
}.translate()

/**
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see ILevelExtension.getDescription
 */
fun levelText(key: ResourceKey<Level>): MutableComponent {
    val location: ResourceLocation = key.location()
    return translatableText(location.toLanguageKey(ILevelExtension.TRANSLATION_PREFIX), location.toString())
}

/**
 * 指定した[色][color]を適応します。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
fun MutableComponent.withStyle(color: HTDefaultColor): MutableComponent =
    this.withStyle { style: Style -> style.withColor(color.textColor) }
