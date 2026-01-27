package hiiragi283.core.api.material

import hiiragi283.core.api.property.HTEmptyPropertyMap
import hiiragi283.core.api.property.HTPropertyMap

/**
 * 素材のプロパティを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
interface HTMaterialManager : Map<HTMaterialKey, HTPropertyMap> {
    /**
     * 指定した[素材][material]がプロパティを保持しているか判定します。
     */
    operator fun contains(material: HTMaterialLike): Boolean = containsKey(material.asMaterialKey())

    /**
     * 指定した[素材][material]のプロパティの一覧を取得します。
     * @return プロパティを保持していない場合は`null`
     */
    operator fun get(material: HTMaterialLike): HTPropertyMap? = get(material.asMaterialKey())

    /**
     * 指定した[素材][material]のプロパティの一覧を取得します。
     * @return プロパティを保持していない場合は[HTEmptyPropertyMap]
     */
    fun getOrEmpty(material: HTMaterialLike): HTPropertyMap = get(material) ?: HTEmptyPropertyMap
}
