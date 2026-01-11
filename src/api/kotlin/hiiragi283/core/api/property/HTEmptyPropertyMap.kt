package hiiragi283.core.api.property

/**
 * 空の[HTPropertyMap]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
object HTEmptyPropertyMap : HTPropertyMap {
    override fun isEmpty(): Boolean = true

    override fun contains(key: HTPropertyKey<*>): Boolean = false

    override fun <T> get(key: HTPropertyKey<T>): T? = null
}
