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
    fun translate(type: HTLangType, value: String): String

    /**
     * @param type 言語の種類
     * @param provider `%s`を置換する翻訳
     * @return '%s'が置換された翻訳名
     */
    fun translate(type: HTLangType, provider: HTLangName): String = translate(type, provider.getTranslatedName(type))

    companion object {
        /**
         * @since 0.17.0
         */
        @JvmField
        val IDENTITY = HTLangPatternProvider { _, value: String -> value }

        /**
         * 指定した[enPattern]と[jaPattern]から[HTLangPatternProvider]の新しいインスタンスを作成します。
         * @since 0.7.0
         */
        @JvmStatic
        fun create(enPattern: String, jaPattern: String): HTLangPatternProvider = create(enPattern, HTLangTypes.JA_JP to jaPattern)

        /**
         * 指定した[enPattern]と[others]から[HTLangPatternProvider]の新しいインスタンスを作成します。
         * @since 0.8.0
         */
        @JvmStatic
        fun create(enPattern: String, vararg others: Pair<HTLangType, String>): HTLangPatternProvider = create(enPattern, mapOf(*others))

        /**
         * 指定した[enPattern]と[others]から[HTLangPatternProvider]の新しいインスタンスを作成します。
         * @since 0.8.0
         */
        @JvmStatic
        fun create(enPattern: String, others: Map<HTLangType, String>): HTLangPatternProvider = HTLangPatternProvider { type: HTLangType, value: String -> (others[type] ?: enPattern).replace("%s", value) }
    }
}
