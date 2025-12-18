package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.jvm.optionals.getOrNull
import kotlin.streams.asSequence

//    HolderGetter    //

fun <T : Any> HolderGetter<T>.getOrNull(key: ResourceKey<T>): Holder<T>? = this.get(key).getOrNull()

fun <T : Any> HolderGetter<T>.getOrNull(tagKey: TagKey<T>): HolderSet<T>? = this.get(tagKey).getOrNull()

//    HolderLookup    //

fun <R : Any, T : Any> HolderLookup.RegistryLookup<R>.getHolderDataMap(type: DataMapType<R, T>): Map<Holder.Reference<R>, T> = this
    .listElementIds()
    .asSequence()
    .mapNotNull { key: ResourceKey<R> ->
        val data: T = this.getData(type, key) ?: return@mapNotNull null
        getOrThrow(key) to data
    }.toMap()

fun <T : Any> HolderLookup.Provider.lookupOrNull(registryKey: RegistryKey<T>): HolderLookup.RegistryLookup<T>? =
    this.lookup(registryKey).getOrNull()

fun <T : Any> HolderLookup.Provider.holderOrNull(key: ResourceKey<T>): Holder<T>? = this.holder(key).getOrNull()

fun <T : Any> HolderLookup.Provider.holderSetOrNull(tagKey: TagKey<T>): HolderSet<T>? =
    this.lookupOrNull(tagKey.registry)?.get(tagKey)?.getOrNull()

//    RegistryAccess    //

fun <T : Any> RegistryAccess.registryOrNull(registryKey: RegistryKey<T>): Registry<T>? = this.registry(registryKey).getOrNull()
