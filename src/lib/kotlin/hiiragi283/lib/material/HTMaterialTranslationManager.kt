package hiiragi283.lib.material

import hiiragi283.lib.collection.PairMapTable
import hiiragi283.lib.collection.Table
import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.data.lang.HTLangType

/**
 * 素材に関する翻訳を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTMaterialTranslationManager {
    /**
     * 翻訳名を取得します。
     * @return 対応する翻訳名がない場合は`null`
     */
    @JvmStatic
    fun getName(type: HTLangType, part: HTMaterialPartKey, material: HTMaterialKey): String? = customName[part, material]?.getTranslatedName(type) ?: _materials[material]?.let { _parts[part]?.translate(type, it) }

    //    Part    //

    @JvmStatic
    val parts: Map<HTMaterialPartKey, HTLangPatternProvider> get() = _parts

    @JvmStatic
    private val _parts: MutableMap<HTMaterialPartKey, HTLangPatternProvider> = hashMapOf()

    /**
     * 部品の翻訳パターンを追加します。
     */
    @JvmStatic
    fun add(part: HTMaterialPartKey, provider: HTLangPatternProvider) {
        check(_parts.put(part, provider) == null) { "Duplicated part name pattern for $part" }
    }

    //    Material    //

    @JvmStatic
    val materials: Map<HTMaterialKey, HTLangName> get() = _materials

    @JvmStatic
    private val _materials: MutableMap<HTMaterialKey, HTLangName> = hashMapOf()

    /**
     * 素材の翻訳名を追加します。
     */
    @JvmStatic
    fun add(material: HTMaterialKey, name: HTLangName) {
        check(_materials.put(material, name) == null) { "Duplicated material name for $material" }
    }

    //    Custom Name    //

    @JvmStatic
    val customName: Table<HTMaterialPartKey, HTMaterialKey, HTLangName> get() = _customName.build()

    @JvmStatic
    private val _customName: Table.Builder<HTMaterialPartKey, HTMaterialKey, HTLangName> = PairMapTable.Builder()

    /**
     * 特定の部品と素材に対する翻訳名を追加します。
     */
    @JvmStatic
    fun add(part: HTMaterialPartKey, material: HTMaterialKey, name: HTLangName) {
        check(_customName.put(part, material, name) == null) { "Duplicated custom name for part $part and material $material" }
    }
}
