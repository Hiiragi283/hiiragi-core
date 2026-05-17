package hiiragi283.core.impl.serialization.network

import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.toLike
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey

/**
 * @suppress
 */
internal class HTHolderLikeStreamCodec<R : Any>(private val registryKey: RegistryKey<R>) : StreamCodec<RegistryFriendlyByteBuf, HTSimpleHolderLike<R>> {
    private val holderCodec: StreamCodec<RegistryFriendlyByteBuf, Holder<R>> = ByteBufCodecs.holderRegistry(registryKey)

    override fun decode(buffer: RegistryFriendlyByteBuf): HTSimpleHolderLike<R> = holderCodec.decode(buffer).toLike()

    override fun encode(buffer: RegistryFriendlyByteBuf, value: HTSimpleHolderLike<R>) {
        value
            .unwrap()
            .onLeft { key: ResourceKey<R> ->
                buffer
                    .registryAccess()
                    .lookupOrThrow(registryKey)
                    .getOrThrow(key)
                    .let { holderCodec.encode(buffer, it) }
            }.onRight { holder: Holder<R> -> holderCodec.encode(buffer, holder) }
    }
}
