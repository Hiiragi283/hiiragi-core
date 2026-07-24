package hiiragi283.core.api.data.lang

/**
 * 言語の種類を表す列挙型クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@JvmInline
value class HTLangType private constructor(val name: String) : Comparable<HTLangType> {
    companion object {
        @JvmStatic
        private val instances: MutableMap<String, HTLangType> = hashMapOf()

        @JvmStatic
        fun of(name: String): HTLangType = instances.computeIfAbsent(name.lowercase(), ::HTLangType)
    }

    override fun compareTo(other: HTLangType): Int = this.name.compareTo(other.name)
}
