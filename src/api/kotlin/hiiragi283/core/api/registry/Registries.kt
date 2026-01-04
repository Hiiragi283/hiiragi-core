package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.jvm.optionals.getOrNull
import kotlin.streams.asSequence

//    HolderLookup    //

fun <R : Any, T : Any> HolderLookup.RegistryLookup<R>.getHolderDataMap(type: DataMapType<R, T>): Map<HTHolderLike<R, R>, T> = this
    .listElementIds()
    .asSequence()
    .mapNotNull { key: ResourceKey<R> ->
        val data: T = this.getData(type, key) ?: return@mapNotNull null
        getOrThrow(key).toLike() to data
    }.toMap()

fun <T : Any> HolderLookup.Provider.holderOrNull(key: ResourceKey<T>): Holder<T>? = this.holder(key).getOrNull()

fun <T : Any> HolderLookup.Provider.holderSetOrNull(tagKey: TagKey<T>): HolderSet<T>? =
    this.lookup(tagKey.registry).flatMap { it.get(tagKey) }.getOrNull()
