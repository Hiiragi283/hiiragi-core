package hiiragi283.core.api.data.lang

/**
 * 翻訳名を返す処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTLangName {
    /**
     * 指定された[言語の種類][type]から翻訳名を返します。
     */
    fun getTranslatedName(type: HTLanguageType): String

    companion object {
        /**
         * @since 0.7.0
         */
        @JvmStatic
        fun create(enName: String, jaName: String): HTLangName = HTLangName { type: HTLanguageType ->
            when (type) {
                HTLanguageType.EN_US -> enName
                HTLanguageType.JA_JP -> jaName
            }
        }
    }
}
