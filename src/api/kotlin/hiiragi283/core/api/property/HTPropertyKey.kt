package hiiragi283.core.api.property

import net.minecraft.resources.ResourceLocation

/**
 * [HTPropertyGetter]のキーとして使用されるクラスです。
 * @param T 対応する値のクラス
 * @param id ユニークな[ID][ResourceLocation]
 * @param defaultValue 対応する値がない場合のデフォルト値
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
class HTPropertyKey<T> private constructor(val id: ResourceLocation, val defaultValue: T) : Comparable<HTPropertyKey<*>> {
    companion object {
        @JvmStatic
        private val instance: MutableMap<ResourceLocation, HTPropertyKey<*>> = hashMapOf()

        /**
         * 指定した[id]から，デフォルト値が`null`となる新しい[HTPropertyKey]のインスタンスを作成します。
         * @throws IllegalStateException 指定した[ID][id]がすでに使用されていた場合
         */
        @JvmStatic
        fun <T : Any> createNullable(id: ResourceLocation): HTPropertyKey<T?> = create(id, null)

        /**
         * 指定した[id]から，デフォルト値が`null`となる新しい[HTPropertyKey]のインスタンスを作成します。
         * @throws IllegalStateException 指定した[ID][id]がすでに使用されていた場合
         */
        @JvmStatic
        fun createFlag(id: ResourceLocation): HTPropertyKey<Unit?> = createNullable(id)

        /**
         * 指定した[id]から，[Set]向けの新しい[HTPropertyKey]のインスタンスを作成します。
         * @throws IllegalStateException 指定した[ID][id]がすでに使用されていた場合
         * @since 0.8.0
         */
        @JvmStatic
        fun <T : Any> createSet(id: ResourceLocation): HTPropertyKey<Set<T>> = create(id, emptySet())

        /**
         * 指定した[id]から，[Map]向けの新しい[HTPropertyKey]のインスタンスを作成します。
         * @param K キーのクラス
         * @param V 値のクラス
         * @throws IllegalStateException 指定した[ID][id]がすでに使用されていた場合
         */
        @JvmStatic
        fun <K : Any, V : Any> createMap(id: ResourceLocation): HTPropertyKey<Map<K, V>> = create(id, emptyMap())

        /**
         * 指定した[id]と[defaultValue]から新しい[HTPropertyKey]のインスタンスを作成します。
         * @throws IllegalStateException 指定した[ID][id]がすでに使用されていた場合
         */
        @JvmStatic
        fun <T> create(id: ResourceLocation, defaultValue: T): HTPropertyKey<T> {
            val key: HTPropertyKey<T> = HTPropertyKey(id, defaultValue)
            check(instance.put(id, key) == null) { "Duplicated material attribute key: $id" }
            return key
        }
    }

    override fun compareTo(other: HTPropertyKey<*>): Int = this.id.compareNamespaced(other.id)

    override fun toString(): String = "HTPropertyKey(id=$id, defaultValue=$defaultValue)"
}
