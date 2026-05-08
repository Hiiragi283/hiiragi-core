package hiiragi283.core.impl.registry

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * [DeferredHolder]向けの[HTHolderLike]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
open class HTDeferredHolderLike<R : Any, T : R>(protected open val holder: DeferredHolder<R, T>) : HTHolderLike<R, T> {
    final override fun unwrap(): Either<ResourceKey<R>, Holder<R>> = Either.left(holder.key!!)

    final override fun get(): T = holder.get()
}
