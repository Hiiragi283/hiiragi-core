package hiiragi283.core.impl.registry

import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey

/**
 * [Holder]向けの[HTSimpleHolderLike]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
@JvmInline
internal value class HTRegistryHolderLike<R : Any>(val holder: Holder<R>) : HTSimpleHolderLike<R> {
    override fun unwrap(): Either<ResourceKey<R>, Holder<R>> = Either.Right(holder)

    override fun get(): R = holder.value()
}
