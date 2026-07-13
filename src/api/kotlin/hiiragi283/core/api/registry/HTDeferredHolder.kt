package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.SupplierWithKey
import hiiragi283.core.api.util.HTTextResult
import hiiragi283.core.api.util.Option
import hiiragi283.core.api.util.toOption
import hiiragi283.core.api.util.toTextResult
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder

typealias HTSimpleDeferredHolder<R> = HTDeferredHolder<R, R>

/**
 * Hiiragi Seriesで使用される[DeferredHolder]の拡張クラスです。
 * @param R レジストリの要素のクラス
 * @param T 要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
open class HTDeferredHolder<R : Any, out T : R> :
    DeferredHolder<R, @UnsafeVariance T>,
    SupplierWithKey<R, T> {
    constructor(key: ResourceKey<R>) : super(key)

    constructor(key: RegistryKey<R>, id: ResourceLocation) : super(key.createKey(id))

    fun getOrNull(): T? = if (this.isBound) get() else null

    fun getResult(): HTTextResult<T> = getOrNull().toTextResult { "Trying to access unbound value: $key" }

    fun asOption(): Option<T> = getOrNull().toOption()

    override fun getId(): ResourceLocation = super<DeferredHolder>.getId()

    override fun getKey(): ResourceKey<R> = super.key!!
}
