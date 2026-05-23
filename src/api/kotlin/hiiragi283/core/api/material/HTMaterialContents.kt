package hiiragi283.core.api.material

import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.tagPrefix
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.text.Text
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

typealias HTSimpleMaterialContents<R, V> = HTMaterialContents<R, HTMaterialContents.SimpleEntry<V>>

/**
 * 素材システムに基づいた要素を管理するインターフェースです。
 * @param R 行のクラス
 * @param V 要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
interface HTMaterialContents<R : Any, V : HTMaterialContents.Entry<*>> : HTTable<R, HTMaterialKey, V> {
    /**
     * 指定した[row]と[material]から対応する値を返します。
     * @return 対応する値がない場合は`null`
     */
    operator fun get(row: R, material: HTMaterialLike): V? = this[row, material.asMaterialKey()]

    /**
     * 指定した[row]と[material]から対応する値を返します。
     * @throws IllegalStateException 対応する値がない場合
     */
    fun getOrThrow(row: R, material: HTMaterialLike): V = get(row, material) ?: error(getErrorMessage(row, material.asMaterialKey()))

    /**
     * 対応する値がない場合のエラーメッセージを作成します。
     */
    fun getErrorMessage(row: R, material: HTMaterialKey): String

    fun column(material: HTMaterialLike): Map<R, V> = this.column(material.asMaterialKey())

    /**
     * [HTMaterialContents]で使用される要素を表すクラスです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Entry<out V> : SupplierWithId<V> {
        /**
         * 既存の要素である場合は`true`
         */
        val isBuiltIn: Boolean

        operator fun component1(): ResourceLocation = getId()

        operator fun component2(): V = get()

        operator fun component3(): Boolean = isBuiltIn
    }

    /**
     * @since 0.13.0
     */
    class SimpleEntry<V : Any>(private val holder: SupplierWithId<V>, override val isBuiltIn: Boolean) :
        Entry<V>,
        SupplierWithId<V> by holder

    /**
     * @since 0.13.0
     */
    class ItemEntry(private val holder: SupplierWithId<Item>, override val isBuiltIn: Boolean) :
        Entry<Item>,
        ItemLike,
        HTIdLike.Translatable,
        SupplierWithId<Item> by holder {
        constructor(item: Item, isBuiltIn: Boolean) : this(item.toLike(), isBuiltIn)

        override fun asItem(): Item = get()

        override val translationKey: String get() = get().descriptionId

        override fun getText(): Text = get().description
    }
}

//    Extensions    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
operator fun <V : HTMaterialContents.Entry<*>> HTMaterialContents<HTPart, V>.get(part: HTPartLike, material: HTMaterialLike): V? = this[part.asPart(), material]

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <V : HTMaterialContents.Entry<*>> HTMaterialContents<HTPart, V>.getOrThrow(part: HTPartLike, material: HTMaterialLike): V = this.getOrThrow(part.asPart(), material)

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
val <V : HTMaterialContents.Entry<*>> HTMaterialContents<HTPart, V>.prefixEntries: Sequence<Triple<HTTagPrefix, HTMaterialKey, V>>
    get() = this
        .entries
        .asSequence()
        .mapNotNull { (part: HTPart, key: HTMaterialKey, entry: V) ->
            val prefix: HTTagPrefix = part.tagPrefix ?: return@mapNotNull null
            Triple(prefix, key, entry)
        }
