package hiiragi283.core.api.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.property.HTEmptyPropertyMap
import hiiragi283.core.api.property.HTPropertyMap

/**
 * 素材のプロパティを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
interface HTMaterialManager {
    companion object {
        /**
         * [HTMaterialManager]のインスタンス
         */
        @JvmField
        val INSTANCE: HTMaterialManager = HiiragiCoreAPI.getService()
    }

    /**
     * 指定した[素材][material]がプロパティを保持しているか判定します。
     */
    operator fun contains(material: HTMaterialLike): Boolean

    /**
     * 指定した[素材][material]のプロパティの一覧を取得します。
     * @return プロパティを保持していない場合は`null`
     */
    operator fun get(material: HTMaterialLike): HTPropertyMap?

    /**
     * プロパティを保持する素材の一覧
     */
    val keys: Set<HTMaterialKey>

    /**
     * 素材とプロパティの組の一覧
     */
    val entries: Set<Map.Entry<HTMaterialKey, HTPropertyMap>>

    /**
     * 素材とプロパティの組の一覧を[Sequence]として返します。
     */
    fun asSequence(): Sequence<Map.Entry<HTMaterialKey, HTPropertyMap>> = entries.asSequence()

    /**
     * 指定した[素材][material]のプロパティの一覧を取得します。
     * @return プロパティを保持していない場合は[HTEmptyPropertyMap]
     */
    fun getOrEmpty(material: HTMaterialLike): HTPropertyMap = get(material) ?: HTEmptyPropertyMap
}
