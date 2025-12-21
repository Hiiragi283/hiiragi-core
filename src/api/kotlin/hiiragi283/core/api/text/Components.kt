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
 */
fun String.toText(): MutableComponent = Component.literal(this)

/**
 * 指定した[文字列][value]を翻訳された[テキスト][MutableComponent]に変換します。
 */
fun translatableText(value: String): MutableComponent = Component.translatable(value)

/**
 * 指定した[文字列][value]と[引数][args]を翻訳された[テキスト][MutableComponent]に変換します。
 */
fun translatableText(value: String, vararg args: Any): MutableComponent = Component.translatable(value, *args)

/**
 * 指定した[Boolean]を翻訳された[テキスト][MutableComponent]に変換します。
 */
fun boolText(value: Boolean): MutableComponent = when (value) {
    true -> HTCommonTranslation.TRUE
    false -> HTCommonTranslation.FALSE
}.translate()

/**
 * @see ILevelExtension.getDescription
 */
fun levelText(key: ResourceKey<Level>): MutableComponent {
    val location: ResourceLocation = key.location()
    return translatableText(location.toLanguageKey(ILevelExtension.TRANSLATION_PREFIX), location.toString())
}
