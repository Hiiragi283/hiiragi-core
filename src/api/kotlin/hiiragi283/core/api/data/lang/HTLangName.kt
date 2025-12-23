package hiiragi283.core.api.data.lang

/**
 * 翻訳名を返す処理を表すインターフェースです。
 */
fun interface HTLangName {
    /**
     * 指定された[言語の種類][type]から翻訳名を返します。
     */
    fun getTranslatedName(type: HTLanguageType): String
}
