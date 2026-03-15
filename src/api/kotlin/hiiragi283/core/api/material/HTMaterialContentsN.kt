package hiiragi283.core.api.material

import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTSimpleHolderLike
import net.minecraft.resources.ResourceKey

/**
 * 素材システムに基づいた要素を管理するインターフェースです。
 * @param R 行のクラス
 * @param V 要素のクラス
 * @param E [HTHolderLike]を継承したクラス
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
interface HTMaterialContentsN<R : Any, V : Any, E: HTHolderLike<V, *>> : HTTable<R, HTMaterialKey, E> {
    /**
     * 指定した[row]と[material]から対応する値を返します。
     * @return 対応する値がない場合は`null`
     */
    operator fun get(row: R, material: HTMaterialLike): E? = this[row, material.asMaterialKey()]

    /**
     * 指定した[row]と[material]から対応する値を返します。
     * @throws IllegalStateException 対応する値がない場合
     */
    fun getOrThrow(row: R, material: HTMaterialLike): E = get(row, material) ?: error(getErrorMessage(row, material.asMaterialKey()))

    /**
     * 対応する値がない場合のエラーメッセージを作成します。
     */
    fun getErrorMessage(row: R, material: HTMaterialKey): String

    fun column(material: HTMaterialLike): Map<R, E> = this.column(material.asMaterialKey())

    /**
     * [HTMaterialContentsN]で使用される要素を表すクラスです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Entry<V: Any> : HTSimpleHolderLike<V> {
        /**
         * 既存の要素である場合は`true`
         */
        val isBuiltIn: Boolean

        operator fun component1(): ResourceKey<V> = getResourceKey()

        operator fun component2(): V = get()

        operator fun component3(): Boolean = isBuiltIn
    }
}
