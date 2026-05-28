package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

/**
 * 指定した[Holder][this]から[ResourceKey]を取得します。
 * @throws IllegalStateException [Holder.unwrapKey]の値が空の場合
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun <R : Any> Holder<R>.getKeyOrThrow(): ResourceKey<R> = this.unwrapKey().orElseThrow { error("Unregistered holder: $this") }

/**
 * 指定した[Holder][this]を[SupplierWithId]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun <T : Any> Holder<T>.toLike(): SupplierWithId<T> = object : SupplierWithId<T> {
    override fun get(): T = this@toLike.value()

    override fun getId(): ResourceLocation = this@toLike.getKeyOrThrow().location()
}

//    HolderSet    //

fun <T : Any> holderSetOf(): HolderSet<T> = HolderSet.empty()

fun <T : Any> holderSetOf(holder: Holder<T>): HolderSet<T> = buildHolderSet { add(holder) }

fun <T : Any> holderSetOf(vararg holders: Holder<T>): HolderSet<T> = buildHolderSet { addAll(holders) }

inline fun <T : Any> buildHolderSet(builderAction: HolderSetBuilder<T>.() -> Unit): HolderSet<T> = HolderSetBuilder<T>().apply(builderAction).build()

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
