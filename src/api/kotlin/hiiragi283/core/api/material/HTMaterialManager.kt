package hiiragi283.core.api.material

import hiiragi283.core.api.property.HTEmptyPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.ResourceLocation

/**
 * 素材のプロパティを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
interface HTMaterialManager : Iterable<HTMaterialManager.Entry> {
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
     * 指定した[素材][material]のプロパティの一覧を取得します。
     * @return プロパティを保持していない場合は[HTEmptyPropertyMap]
     */
    fun getOrEmpty(material: HTMaterialLike): HTPropertyMap = get(material) ?: HTEmptyPropertyMap

    /**
     * 登録された[素材][HTMaterialKey]の一覧を取得します。
     */
    val keys: Set<HTMaterialKey>

    /**
     * 登録された[エントリ][Entry]の一覧を取得します。
     */
    val entries: Set<Entry>

    override fun iterator(): Iterator<Entry> = entries.iterator()

    /**
     * [HTMaterialLike]と[HTPropertyMap]を束ねたインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.9.0
     */
    interface Entry :
        HTIdLike,
        HTMaterialLike,
        HTPropertyMap {
        override fun getId(): ResourceLocation = asMaterialId()
    }
}
