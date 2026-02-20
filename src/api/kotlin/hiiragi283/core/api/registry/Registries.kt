package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.jvm.optionals.getOrNull
import kotlin.streams.asSequence

//    HolderLookup    //

/**
 * @since 0.9.0
 */
fun <R : Any> HolderLookup<R>.asSequence(): Sequence<HTSimpleHolderLikeDelegate<R>> = this
    .listElements()
    .map(Holder<R>::toLike)
    .asSequence()

/**
 * @since 0.9.0
 */
fun HolderLookup<Block>.asBlockSequence(): Sequence<HTBlockHolderLike<*>> = this.asSequence()

/**
 * @since 0.10.0
 */
fun HolderLookup<Fluid>.asFluidSequence(): Sequence<HTFluidHolderLike<*>> = this.asSequence()

/**
 * @since 0.9.0
 */
fun HolderLookup<Item>.asItemSequence(): Sequence<HTItemHolderLike<*>> = this
    .listElements()
    .map(HTItemHolderLike.Companion::of)
    .asSequence()

fun <R : Any, T : Any> HolderLookup<R>.getHolderDataMap(type: DataMapType<R, T>): Map<HTSimpleHolderLikeDelegate<R>, T> = this
    .asSequence()
    .mapNotNull { holder: HTSimpleHolderLikeDelegate<R> ->
        val data: T = holder.getHolder().getData(type) ?: return@mapNotNull null
        holder to data
    }.toMap()

fun <T : Any> HolderLookup.Provider.holderSetOrNull(tagKey: TagKey<T>): HolderSet<T>? =
    this.lookup(tagKey.registry).flatMap { it.get(tagKey) }.getOrNull()
