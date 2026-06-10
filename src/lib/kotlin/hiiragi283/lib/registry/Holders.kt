package hiiragi283.lib.registry

import hiiragi283.lib.resource.SupplierWithId
import net.minecraft.core.Holder
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
