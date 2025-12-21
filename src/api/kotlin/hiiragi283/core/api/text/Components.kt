package hiiragi283.core.api.text

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
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
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see ILevelExtension.getDescription
 */
fun levelText(key: ResourceKey<Level>): MutableComponent {
    val location: ResourceLocation = key.location()
    return translatableText(location.toLanguageKey(ILevelExtension.TRANSLATION_PREFIX), location.toString())
}
