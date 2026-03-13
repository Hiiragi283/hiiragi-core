package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.toId
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.jvm.optionals.getOrNull
import kotlin.streams.asSequence

//    HolderLookup    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <R : Any, T : Any> HolderLookup<R>.getDataSequence(type: DataMapType<R, T>): Sequence<Pair<HTSimpleHolderLike<R>, T>> = this
    .listElements()
    .asSequence()
    .mapNotNull { holder: Holder.Reference<R> ->
        val data: T = holder.getData(type) ?: return@mapNotNull null
        holder.toLike() to data
    }

fun <R : Any, T : Any> HolderLookup<R>.getHolderDataMap(type: DataMapType<R, T>): Map<HTSimpleHolderLike<R>, T> =
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
fun <R : Any> DeferredRegister<R>.asSequence(): Sequence<HTHolderLike<R, *>> = this.entries
    .asSequence()
    .map { it.toLike() }
