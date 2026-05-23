package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder

typealias HTSimpleDeferredHolder<R> = HTDeferredHolder<R, R>

open class HTDeferredHolder<R : Any, out T : R> :
    DeferredHolder<R, @UnsafeVariance T>,
    SupplierWithId<T> {
    constructor(key: ResourceKey<R>) : super(key)

    constructor(key: RegistryKey<R>, id: ResourceLocation) : super(key.createKey(id))
}
