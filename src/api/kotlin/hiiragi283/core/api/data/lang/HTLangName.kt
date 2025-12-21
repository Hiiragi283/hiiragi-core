package hiiragi283.core.api.data.lang

/**
 * 翻訳した文字列を返す処理を表すインターフェース
 */
fun interface HTLangName {
    fun getTranslatedName(type: HTLanguageType): String
}
