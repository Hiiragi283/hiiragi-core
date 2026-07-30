package hiiragi283.core.api.property

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.network.codec.StreamCodec

interface HTPropertyManager<K, E : HTPropertyManager.Entry<K>> : Iterable<E> {
    companion object {
        @JvmStatic
        fun <K, E : Entry<K>> codec(keyCodec: Codec<K>, instance: () -> HTPropertyManager<K, E>, errorMessage: (K) -> String): Codec<E> = keyCodec.comapFlatMap(
            { key: K ->
                instance()
                    .firstOrNull { it.key == key }
                    ?.let { DataResult.success(it) }
                    ?: DataResult.error { errorMessage(key) }
            },
            { it.key },
        )

        @JvmStatic
        fun <B, K, E : Entry<K>> streamCodec(keyCodec: StreamCodec<B, K>, instance: () -> HTPropertyManager<K, E>, errorMessage: (K) -> String): StreamCodec<B, E> = keyCodec.map(
            { key: K -> instance().firstOrNull { it.key == key } ?: errorMessage(key).let(::error) },
            { it.key },
        )
    }

    operator fun contains(key: K): Boolean

    operator fun get(key: K): E?

    fun getOrEmpty(key: K): HTPropertyGetter = get(key) ?: HTPropertyGetter.NOTHING

    fun getOrThrow(key: K): E = get(key) ?: error("Missing entry: $key")

    val keys: Set<K>

    val entries: Collection<E>

    override fun iterator(): Iterator<E> = entries.iterator()

    interface Entry<K> : HTPropertyGetter {
        val key: K
    }
}
