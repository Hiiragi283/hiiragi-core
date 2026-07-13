package hiiragi283.core.api.serialization.network

import hiiragi283.core.api.util.DFUEither
import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.Option
import hiiragi283.core.api.util.java
import hiiragi283.core.api.util.kotlin
import hiiragi283.core.api.util.toOption
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <B : ByteBuf, V : Any, C : Collection<V>> StreamCodec<B, V>.toCollection(factory: (Int) -> C): StreamCodec<B, C> = ByteBufCodecs.collection(factory, this)

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.listOf(): StreamCodec<B, List<V>> = this.toCollection(::ArrayList)

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.setOf(): StreamCodec<B, Set<V>> = this.toCollection(::LinkedHashSet)

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.recover(onFailure: (Throwable) -> V): StreamCodec<B, V> = this.apply { base: StreamCodec<B, V> ->
    object : StreamCodec<B, V> {
        override fun decode(buffer: B): V = runCatching { base.decode(buffer) }.getOrElse(onFailure)

        override fun encode(buffer: B, value: V) {
            base.encode(buffer, value)
        }
    }
}

@JvmName("convertToEither")
fun <B : ByteBuf, L : Any, R : Any> StreamCodec<B, DFUEither<L, R>>.convert(): StreamCodec<B, Either<L, R>> = this.map({ it.kotlin }, { it.java })

fun <B : ByteBuf, V : Any> StreamCodec<B, V>.asOption(): StreamCodec<B, Option<V>> = object : StreamCodec<B, Option<V>> {
    override fun encode(output: B, value: Option<V>) {
        value.fold(
            { output.writeBoolean(false) },
            {
                output.writeBoolean(true)
                this@asOption.encode(output, it)
            },
        )
    }

    override fun decode(input: B): Option<V> = when (input.readBoolean()) {
        true -> this@asOption.decode(input).toOption()
        false -> Option.none()
    }
}
