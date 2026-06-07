package hiiragi283.lib.material

import hiiragi283.lib.tag.HTTagPrefix

data object HTPartTagManager {
    @JvmStatic
    val prefixes: Map<HTMaterialPartKey, HTTagPrefix> get() = _prefixes

    @JvmStatic
    private val _prefixes: MutableMap<HTMaterialPartKey, HTTagPrefix> = hashMapOf()

    @JvmStatic
    operator fun get(part: HTMaterialPartKey): HTTagPrefix? = _prefixes[part]

    @JvmStatic
    fun add(part: HTMaterialPartKey, prefix: HTTagPrefix) {
        check(_prefixes.put(part, prefix) == null) { "Duplicated tag prefix for part $part" }
    }

    @JvmStatic
    operator fun set(part: HTMaterialPartKey, prefix: HTTagPrefix) {
        add(part, prefix)
    }
}
