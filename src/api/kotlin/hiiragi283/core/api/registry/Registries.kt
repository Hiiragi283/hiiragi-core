package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.streams.asSequence

//    HolderLookup    //

fun <R : Any, T : Any> HolderLookup.RegistryLookup<R>.getHolderDataMap(type: DataMapType<R, T>): Map<Holder.Reference<R>, T> = this
    .listElementIds()
    .asSequence()
    .mapNotNull { key: ResourceKey<R> ->
        val data: T = this.getData(type, key) ?: return@mapNotNull null
        key to data
    }.associate { (key: ResourceKey<R>, data: T) -> this.getOrThrow(key) to data }
