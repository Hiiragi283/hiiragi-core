package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.jvm.optionals.getOrNull
import kotlin.streams.asSequence

//    HolderLookup    //

fun <R : Any> HolderLookup<R>.asSequence(): Sequence<HTHolderLike.HolderDelegate<R, R>> = this
    .listElements()
    .map(Holder<R>::toLike)
    .asSequence()

fun HolderLookup<Block>.asBlockSequence(): Sequence<HTBlockHolderLike<*, *>> = this
    .listElementIds()
    .map(HTBlockHolderLike.Companion::of)
    .asSequence()

fun HolderLookup<Item>.asItemSequence(): Sequence<HTItemHolderLike<*>> = this
    .listElementIds()
    .map(HTItemHolderLike.Companion::of)
    .asSequence()

fun <R : Any, T : Any> HolderLookup<R>.getHolderDataMap(type: DataMapType<R, T>): Map<HTHolderLike<R, R>, T> = this
    .asSequence()
    .mapNotNull { holder: HTHolderLike.HolderDelegate<R, R> ->
        val data: T = holder.getHolder().getData(type) ?: return@mapNotNull null
        holder to data
    }.toMap()

fun <T : Any> HolderLookup.Provider.holderSetOrNull(tagKey: TagKey<T>): HolderSet<T>? =
    this.lookup(tagKey.registry).flatMap { it.get(tagKey) }.getOrNull()
