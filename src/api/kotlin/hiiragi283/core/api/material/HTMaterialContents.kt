package hiiragi283.core.api.material

import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.tagPrefix
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

/**
 * 素材システムに基づいた要素を管理するインターフェースです。
 * @param R 行のクラス
 * @param V 要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
interface HTMaterialContents<R : Any, V : Any> : HTTable<R, HTMaterialKey, HTMaterialContents.Entry<V>> {
    companion object {
        @JvmStatic
        fun blockEntry(block: Block, isBuiltIn: Boolean): Entry<Block> = Entry(block.toLike(), isBuiltIn)

        @JvmStatic
        fun fluidEntry(fluid: Fluid, isBuiltIn: Boolean): Entry<Fluid> = Entry(fluid.toLike(), isBuiltIn)

        @JvmStatic
        fun itemEntry(item: Item, isBuiltIn: Boolean): Entry<Item> = Entry(item.toLike(), isBuiltIn)
    }

    /**
     * 指定した[row]と[material]から対応する値を返します。
     * @return 対応する値がない場合は`null`
     */
    operator fun get(row: R, material: HTMaterialLike): Entry<V>? = this[row, material.asMaterialKey()]

    /**
     * 指定した[row]と[material]から対応する値を返します。
     * @throws IllegalStateException 対応する値がない場合
     */
    fun getOrThrow(row: R, material: HTMaterialLike): Entry<V> = get(row, material) ?: error(getErrorMessage(row, material.asMaterialKey()))

    /**
     * 対応する値がない場合のエラーメッセージを作成します。
     */
    fun getErrorMessage(row: R, material: HTMaterialKey): String

    fun column(material: HTMaterialLike): Map<R, Entry<V>> = this.column(material.asMaterialKey())

    /**
     * [HTMaterialContents]で使用される要素を表すクラスです。
     * @param isBuiltIn 既存の要素である場合は`true`
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    class Entry<V : Any>(private val holder: HTHolderLike<V, *>, private val isBuiltIn: Boolean) : HTSimpleHolderLike<V> {
        override fun unwrap(): Either<ResourceKey<V>, Holder<V>> = holder.unwrap()

        override fun get(): V = holder.get()

        operator fun component1(): ResourceKey<V> = getResourceKey()

        operator fun component2(): V = get()

        operator fun component3(): Boolean = isBuiltIn
    }
}

//    Extensions    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
operator fun <V : Any> HTMaterialContents<HTPart, V>.get(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.Entry<V>? =
    this[part.asPart(), material]

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <V : Any> HTMaterialContents<HTPart, V>.getOrThrow(part: HTPartLike, material: HTMaterialLike): HTMaterialContents.Entry<V> =
    this.getOrThrow(part.asPart(), material)

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
val <V : Any> HTMaterialContents<HTPart, V>.prefixEntries: Sequence<Triple<HTTagPrefix, HTMaterialKey, HTMaterialContents.Entry<V>>>
    get() = this
        .entries
        .asSequence()
        .mapNotNull { (part: HTPart, key: HTMaterialKey, entry: HTMaterialContents.Entry<V>) ->
            val prefix = part.tagPrefix ?: return@mapNotNull null
            Triple(prefix, key, entry)
        }
