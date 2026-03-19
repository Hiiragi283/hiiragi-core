package hiiragi283.core.common.registry

import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

abstract class HTBasicHolderLike<R : Any, T : R>(protected val key: ResourceKey<R>) : HTHolderLike<R, T> {
    constructor(registryKey: RegistryKey<R>, id: Identifier) : this(registryKey.createKey(id))

    final override fun unwrap(): Either<ResourceKey<R>, Holder<R>> = Either.Left(key)
}
