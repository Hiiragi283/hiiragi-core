package hiiragi283.core.api.material.attribute

/**
 * ブロックの作成に要求される個数を管理する[HTMaterialAttribute]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
enum class HTStorageBlockMaterialAttribute(val baseCount: Int, val pattern: List<String>) : HTMaterialAttribute {
    SINGLE(1, "A"),
    TWO_BY_TWO(4, "AA", "AB"),
    THREE_BY_THREE(9, "AAA", "ABA", "AAA"),
    ;

    constructor(baseCount: Int, vararg pattern: String) : this(baseCount, pattern.toList())
}
