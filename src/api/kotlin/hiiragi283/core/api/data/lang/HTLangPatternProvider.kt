package hiiragi283.core.api.data.lang

/**
 * 受け取った文字列で`%s`を置換した文字列を返す処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTLangPatternProvider {
    /**
     * @param type 言語の種類
     * @param value `%s`を置換する文字列
     * @return '%s'が置換された翻訳名
     */
    fun translate(type: HTLanguageType, value: String): String

    /**
     * @param type 言語の種類
     * @param provider `%s`を置換する翻訳
     * @return '%s'が置換された翻訳名
     */
    fun translate(type: HTLanguageType, provider: HTLangName): String = translate(type, provider.getTranslatedName(type))

    companion object {
        /**
         * @since 0.7.0
         */
        @JvmStatic
        fun create(enPattern: String, jaPattern: String): HTLangPatternProvider =
            HTLangPatternProvider { type: HTLanguageType, value: String ->
                when (type) {
                    HTLanguageType.EN_US -> enPattern
                    HTLanguageType.JA_JP -> jaPattern
                }.replace("%s", value)
            }
    }
}
