package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
import java.util.function.Supplier

@Suppress("CAST_NEVER_SUCCEEDS")
interface HTHolderLike<R : Any, T : R> :
    HTKeyLike<R>,
    Supplier<T>
