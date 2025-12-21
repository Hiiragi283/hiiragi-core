package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
import net.minecraft.resources.ResourceKey
import java.util.function.Supplier

/**
 * [ResourceKey]と値を保持する[HTKeyLike]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTDeferredHolder
 * @see HTFluidWithTag
 * @see HTItemHolderLike
 */
@Suppress("CAST_NEVER_SUCCEEDS")
interface HTHolderLike<R : Any, T : R> :
    HTKeyLike<R>,
    Supplier<T>
