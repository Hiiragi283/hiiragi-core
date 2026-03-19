package hiiragi283.core.api.data.lang

import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

sealed class HTLanguageProvider(output: PackOutput, modid: String, locale: String) : LanguageProvider(output, modid, locale) {
    /**
     * [HTHasTranslationKey.translationKey]に基づいて翻訳名を追加します。
     */
    fun add(translatable: HTHasTranslationKey, value: String) {
        add(translatable.translationKey, value)
    }

    abstract class English(output: PackOutput, modid: String) : HTLanguageProvider(output, modid, "en_us")

    abstract class Japanese(output: PackOutput, modid: String) : HTLanguageProvider(output, modid, "ja_jp")
}
