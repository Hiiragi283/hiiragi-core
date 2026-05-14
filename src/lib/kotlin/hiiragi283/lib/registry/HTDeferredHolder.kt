package hiiragi283.lib.registry

import hiiragi283.lib.resource.SupplierWithId
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredHolder

open class HTDeferredHolder<R : Any, T : R> :
    DeferredHolder<R, T>,
    SupplierWithId<T> {
    constructor(key: ResourceKey<R>) : super(key)

    constructor(key: RegistryKey<R>, id: Identifier) : super(key.createKey(id))
}
