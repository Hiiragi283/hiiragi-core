package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.streams.asSequence

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
