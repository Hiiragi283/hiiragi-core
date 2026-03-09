package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.toId
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.DeferredRegister
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
    .map(Holder<Item>::toItemLike)
    .asSequence()

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <R : Any, T : Any> HolderLookup<R>.getDataSequence(type: DataMapType<R, T>): Sequence<Pair<HTSimpleHolderLikeDelegate<R>, T>> = this
    .asSequence()
    .mapNotNull { holder: HTSimpleHolderLikeDelegate<R> ->
        val data: T = holder.getHolder().getData(type) ?: return@mapNotNull null
        holder to data
    }

fun <R : Any, T : Any> HolderLookup<R>.getHolderDataMap(type: DataMapType<R, T>): Map<HTSimpleHolderLikeDelegate<R>, T> =
    this.getDataSequence(type).toMap()

fun <T : Any> HolderLookup.Provider.holderSetOrNull(tagKey: TagKey<T>): HolderSet<T>? =
    this.lookup(tagKey.registry).flatMap { it.get(tagKey) }.getOrNull()

//    DeferredRegister    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun DeferredRegister<*>.createId(path: String): ResourceLocation = this.namespace.toId(path)

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun DeferredRegister<*>.addAlias(from: String, to: String) {
    this.addAlias(this.createId(from), this.createId(to))
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <R : Any> DeferredRegister<R>.asSequence(): Sequence<HTHolderLike.HolderDelegate<R, *>> = this.entries
    .asSequence()
    .map { it.toLike() }
