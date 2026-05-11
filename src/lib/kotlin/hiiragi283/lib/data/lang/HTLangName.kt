package hiiragi283.lib.data.lang

/**
 * 翻訳名を返す処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTLangName {
    /**
     * 指定された[言語の種類][type]から翻訳名を返します。
     */
    fun getTranslatedName(type: HTLangType): String

    companion object {
        /**
         * 指定した[enName]と[jaName]から新しい[HTLangName]のインスタンスを作成します。
         * @since 0.7.0
         */
        @JvmStatic
        fun create(enName: String, jaName: String): HTLangName = create(enName, HTLangTypes.JA_JP to jaName)

        /**
         * 指定した[enName]と[others]から新しい[HTLangName]のインスタンスを作成します。
         * @since 0.8.0
         */
        @JvmStatic
        fun create(enName: String, vararg others: Pair<HTLangType, String>): HTLangName = create(enName, mapOf(*others))

        /**
         * 指定した[enName]と[others]から新しい[HTLangName]のインスタンスを作成します。
         * @since 0.8.0
         */
        @JvmStatic
        fun create(enName: String, others: Map<HTLangType, String>): HTLangName = HTLangName { type: HTLangType -> others[type] ?: enName }
    }
}
