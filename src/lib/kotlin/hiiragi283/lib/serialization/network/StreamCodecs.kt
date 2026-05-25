package hiiragi283.lib.serialization.network

import hiiragi283.lib.util.DFUEither
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.java
import hiiragi283.lib.util.kotlin
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.Optional

//    Collection    //

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
fun <B : ByteBuf, V : Any> StreamCodec<B, V>.toOptional(): StreamCodec<B, Optional<V>> = ByteBufCodecs.optional(this)

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

@JvmName("convertToOption")
fun <B : ByteBuf, V : Any> StreamCodec<B, Optional<V>>.convert(): StreamCodec<B, Option<V>> = this.map({ it.kotlin }, { it.java })
