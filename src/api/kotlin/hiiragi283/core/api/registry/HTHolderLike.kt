package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import java.util.function.Supplier

/**
 * [ResourceKey]と値を保持する[HTKeyLike]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@Suppress("CAST_NEVER_SUCCEEDS")
interface HTHolderLike<R : Any, T : R> :
    HTKeyLike<R>,
    Supplier<T> {
    companion object {
        /**
         * @since 0.8.0
         */
        @JvmStatic
        fun <R : Any, H : HTHolderLike<R, *>> keyCodec(registryKey: RegistryKey<R>, factory: (ResourceKey<R>) -> H): BiCodec<ByteBuf, H> =
            VanillaBiCodecs.resourceKey(registryKey).xmap(factory, HTHolderLike<R, *>::getResourceKey)

        /**
         * @since 0.8.0
         */
        @JvmStatic
        fun <R : Any, H : HolderDelegate<R, *>> holderCodec(
            registryKey: RegistryKey<R>,
            factory: (Holder<R>) -> H,
        ): BiCodec<RegistryFriendlyByteBuf, H> = VanillaBiCodecs.holder(registryKey).xmap(factory, HolderDelegate<R, *>::getHolder)
    }

    /**
     * [Holder]を保持する[HolderDelegate]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.6.0
     * @see HTDeferredHolder
     * @see HTFluidContent
     */
    interface HolderDelegate<R : Any, T : R> :
        HTHolderLike<R, T>,
        HTKeyLike.HolderDelegate<R>
}
