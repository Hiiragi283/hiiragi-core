package hiiragi283.lib.collection

import java.util.EnumMap

inline fun <reified K : Enum<K>, V> enumMapOf(): Map<K, V> = EnumMap<K, V>(K::class.java)

inline fun <reified K : Enum<K>, V> enumMapOf(vararg pairs: Pair<K, V>): Map<K, V> = mutableMapOf(*pairs)

inline fun <reified K : Enum<K>, V> mutableEnumMapOf(): MutableMap<K, V> = EnumMap<K, V>(K::class.java)

inline fun <reified K : Enum<K>, V> mutableEnumMapOf(vararg pairs: Pair<K, V>): MutableMap<K, V> = pairs.toMap(mutableEnumMapOf())
