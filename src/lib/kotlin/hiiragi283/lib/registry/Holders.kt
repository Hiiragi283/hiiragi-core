package hiiragi283.lib.registry

import hiiragi283.lib.resource.SimpleSupplierWithKey
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey

/**
 * この[Holder][this]から[ResourceKey]を取得します。
 * @param R 保持する値のクラス
 * @throws IllegalStateException [Holder.unwrapKey]の値が空の場合
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <R : Any> Holder<R>.getKeyOrThrow(): ResourceKey<R> = this.unwrapKey().orElseThrow { error("Unregistered holder: $this") }

/**
 * この[Holder][this]を[SimpleSupplierWithKey]に変換します。
 * @param R 保持する値のクラス
 * @throws IllegalStateException [Holder.kind]が[Holder.Kind.DIRECT]の場合
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <R : Any> Holder<R>.toLike(): SimpleSupplierWithKey<R> = when (this) {
    is HTDeferredHolder<R, *> -> this
    else -> when (this.kind()) {
        Holder.Kind.REFERENCE -> HolderWithKey(this)
        Holder.Kind.DIRECT -> error("Cannot convert direct holder to SupplierWithId")
    }
}

@JvmInline
private value class HolderWithKey<R : Any>(private val holder: Holder<R>) : SimpleSupplierWithKey<R> {
    override fun get(): R = holder.value()

    override fun getKey(): ResourceKey<R> = holder.getKeyOrThrow()
}
