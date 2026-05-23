package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.streams.asSequence

//    HolderSet    //

/**
 * [Holder]の一覧を[HolderSet]に変換します。
 * @param T 要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <T : Any> Iterable<Holder<T>>.toHolderSet(): HolderSet<T> = HolderSet.direct(this.toList())

/**
 * [Holder]の配列を[HolderSet]に変換します。
 * @param T 要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <T : Any> Array<Holder<T>>.toHolderSet(): HolderSet<T> = HolderSet.direct(this.toList())

/**
 * [Holder]の一覧を[HolderSet]に変換します。
 * @param T 要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <T : Any> Sequence<Holder<T>>.toHolderSet(): HolderSet<T> = HolderSet.direct(this.toList())

//    HolderLookup    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <R : Any, T : Any> HolderLookup<R>.getDataSequence(type: DataMapType<R, T>): Sequence<Pair<SupplierWithId<R>, T>> = this
    .listElements()
    .asSequence()
    .mapNotNull { holder: Holder.Reference<R> ->
        val data: T = holder.getData(type) ?: return@mapNotNull null
        holder.toLike() to data
    }

fun <R : Any, T : Any> HolderLookup<R>.getHolderDataMap(type: DataMapType<R, T>): Map<SupplierWithId<R>, T> = this.getDataSequence(type).toMap()
