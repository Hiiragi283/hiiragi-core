package hiiragi283.core.impl.serialization.codec

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Lifecycle
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.toLike
import net.minecraft.core.HolderGetter
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceKey
import kotlin.jvm.optionals.getOrNull

/**
 * @suppress
 */
internal class HTHolderLikeCodec<R : Any>(private val registryKey: RegistryKey<R>) : Codec<HTSimpleHolderLike<R>> {
    private val keyCodec: Codec<ResourceKey<R>> = ResourceKey.codec(registryKey)

    override fun <T : Any> encode(input: HTSimpleHolderLike<R>, ops: DynamicOps<T>, prefix: T): DataResult<T> = input
        .getResourceKey()
        .let { keyCodec.encode(it, ops, prefix) }

    override fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<HTSimpleHolderLike<R>, T>> {
        if (ops is RegistryOps<T>) {
            val getter: HolderGetter<R>? = ops.getter(registryKey).getOrNull()
            if (getter != null) {
                return keyCodec
                    .decode(ops, input)
                    .flatMap { pair: Pair<ResourceKey<R>, T> ->
                        val key: ResourceKey<R> = pair.first
                        getter
                            .get(pair.first)
                            .map { it.toLike() }
                            .map { DataResult.success(it) }
                            .orElseGet { DataResult.error { "Failed to get element ${key.location()}" } }
                            .map { Pair.of(it, pair.second) }
                            .setLifecycle(Lifecycle.stable())
                    }
            }
        }
        return DataResult.error { "Can't access registry $registryKey" }
    }
}
