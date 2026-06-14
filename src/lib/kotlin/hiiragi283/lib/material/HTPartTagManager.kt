package hiiragi283.lib.material

import hiiragi283.lib.tag.HTTagPrefix

/**
 * 部品とタグのプレフィックスを管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTPartTagManager {
    @JvmStatic
    val prefixes: Map<HTMaterialPartKey, HTTagPrefix> get() = _prefixes

    @JvmStatic
    private val _prefixes: MutableMap<HTMaterialPartKey, HTTagPrefix> = hashMapOf()

    /**
     * 部品に対応するタグのプレフィックスを取得します。
     * @return 対応するプレフィックスがない場合は`null`
     */
    @JvmStatic
    operator fun get(part: HTMaterialPartKey): HTTagPrefix? = _prefixes[part]

    /**
     * 部品に対応するタグのプレフィックスを追加します。
     * @throws IllegalStateException 同じ部品に対してタグのプレフィックスが登録された場合
     */
    @JvmStatic
    fun add(part: HTMaterialPartKey, prefix: HTTagPrefix) {
        check(_prefixes.put(part, prefix) == null) { "Duplicated tag prefix for part $part" }
    }

    @JvmStatic
    operator fun set(part: HTMaterialPartKey, prefix: HTTagPrefix) {
        add(part, prefix)
    }
}
