@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.registry

import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.util.HTBuilderMarker
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * 指定した[Holder][this]から[ResourceKey]を取得します。
 * @throws IllegalStateException [unwrapKey]の値が空の場合
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun <R : Any> Holder<R>.getKeyOrThrow(): ResourceKey<R> = this.unwrapKey().orElseThrow { error("Unregistered holder: $this") }

/**
 * 指定した[Holder][this]を[SupplierWithId]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun <T : Any> Holder<T>.toLike(): SupplierWithId<T> = when (this.kind()) {
    Holder.Kind.REFERENCE -> HolderWithId(this)
    Holder.Kind.DIRECT -> error("Cannot convert direct holder to SupplierWithId")
}

@JvmInline
private value class HolderWithId<T : Any>(private val holder: Holder<T>) : SupplierWithId<T> {
    override fun get(): T = holder.value()

    override fun getId(): Identifier = holder.getKeyOrThrow().identifier()
}

//    HolderSet    //

fun <T : Any> holderSetOf(): HolderSet<T> = HolderSet.empty()

fun <T : Any> holderSetOf(holder: Holder<T>): HolderSet<T> = buildHolderSet { add(holder) }

fun <T : Any> holderSetOf(vararg holders: Holder<T>): HolderSet<T> = buildHolderSet { addAll(holders) }

inline fun <T : Any> buildHolderSet(builderAction: HolderSetBuilder<T>.() -> Unit): HolderSet<T> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return HolderSetBuilder<T>().apply(builderAction).build()
}

@HTBuilderMarker
class HolderSetBuilder<T : Any> {
    private val holders: MutableList<Holder<T>> = mutableListOf()

    fun add(holder: Holder<T>) {
        val delegate: Holder<T> = holder.delegate
        check(delegate is Holder.Reference<T>) { "Holder $holder cannot be serialized" }
        this.holders.add(delegate)
    }

    fun addAll(holders: Iterable<Holder<T>>) {
        holders.forEach(::add)
    }

    fun addAll(holders: Array<out Holder<T>>) {
        holders.forEach(::add)
    }

    fun addAll(holders: Sequence<Holder<T>>) {
        holders.forEach(::add)
    }

    operator fun plusAssign(holder: Holder<T>) {
        this.add(holder)
    }

    operator fun plusAssign(holders: Iterable<Holder<T>>) {
        this.addAll(holders)
    }

    operator fun plusAssign(holders: Sequence<Holder<T>>) {
        this.addAll(holders)
    }

    @PublishedApi
    internal fun build(): HolderSet<T> = when {
        holders.isEmpty() -> HolderSet.empty()
        else -> HolderSet.direct(holders)
    }
}
