package hiiragi283.lib.material

import hiiragi283.lib.collection.PairMapTable
import hiiragi283.lib.collection.Table
import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.data.lang.HTLangType

data object HTMaterialTranslationManager {
    @JvmStatic
    fun getName(type: HTLangType, part: HTMaterialPartKey, material: HTMaterialKey): String? = customName[part, material]?.getTranslatedName(type) ?: _materials[material]?.let { _parts[part]?.translate(type, it) }

    //    Part    //

    @JvmStatic
    val parts: Map<HTMaterialPartKey, HTLangPatternProvider> get() = _parts

    @JvmStatic
    private val _parts: MutableMap<HTMaterialPartKey, HTLangPatternProvider> = hashMapOf()

    @JvmStatic
    fun add(part: HTMaterialPartKey, provider: HTLangPatternProvider) {
        check(_parts.put(part, provider) == null) { "Duplicated part name pattern for $part" }
    }

    //    Material    //

    @JvmStatic
    val materials: Map<HTMaterialKey, HTLangName> get() = _materials

    @JvmStatic
    private val _materials: MutableMap<HTMaterialKey, HTLangName> = hashMapOf()

    @JvmStatic
    fun add(material: HTMaterialKey, name: HTLangName) {
        check(_materials.put(material, name) == null) { "Duplicated material name for $material" }
    }

    //    Custom Name    //

    @JvmStatic
    val customName: Table<HTMaterialPartKey, HTMaterialKey, HTLangName> get() = _customName.build()

    @JvmStatic
    private val _customName: Table.Builder<HTMaterialPartKey, HTMaterialKey, HTLangName> = PairMapTable.Builder()

    @JvmStatic
    fun add(part: HTMaterialPartKey, material: HTMaterialKey, name: HTLangName) {
        check(_customName.put(part, material, name) == null) { "Duplicated custom name for part $part and material $material" }
    }
}
