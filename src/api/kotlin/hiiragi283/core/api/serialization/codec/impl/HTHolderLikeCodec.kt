package hiiragi283.core.api.serialization.codec.impl

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Lifecycle
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.toLike
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceKey
import java.util.Optional

internal class HTHolderLikeCodec<R : Any>(private val registryKey: RegistryKey<R>) : Codec<HTSimpleHolderLike<R>> {
    private val keyCodec: Codec<ResourceKey<R>> = ResourceKey.codec(registryKey)
    private val holderCodec: Codec<Holder<R>> = RegistryFixedCodec.create(registryKey)

    override fun <T : Any> encode(input: HTSimpleHolderLike<R>, ops: DynamicOps<T>, prefix: T): DataResult<T> = input
        .unwrap()
        .map(
            { key: ResourceKey<R> -> keyCodec.encode(key, ops, prefix) },
            { holder: Holder<R> -> holderCodec.encode(holder, ops, prefix) },
        )

    override fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<HTSimpleHolderLike<R>, T>> {
        if (ops is RegistryOps<T>) {
            val optional: Optional<HolderGetter<R>> = ops.getter(registryKey)
            if (optional.isPresent) {
                val getter: HolderGetter<R> = optional.get()
                return keyCodec
                    .decode(ops, input)
                    .flatMap { pair: Pair<ResourceKey<R>, T> ->
                        val key: ResourceKey<R> = pair.first
                        getter
                            .get(pair.first)
                            .map { it.toLike() }
                            .map { DataResult.success(it) }
                            .orElseGet {
                                DataResult.error { "Failed to get element ${key.location()}" }
                            }.map { Pair.of(it, pair.second) }
                            .setLifecycle(Lifecycle.stable())
                    }
            }
        }
        return DataResult.error { "Can't access registry $registryKey" }
    }
}
