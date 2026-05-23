package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.core.Holder
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
