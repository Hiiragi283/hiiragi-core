package hiiragi283.core.api.serialization.codec.impl

import hiiragi283.core.api.monad.Either
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

/**
 * @see net.minecraft.network.codec.ByteBufCodecs.either
 */
class HTEitherStreamCodec<B : ByteBuf, A : Any, B1 : Any>(
    private val left: StreamCodec<in B, A>,
    private val right: StreamCodec<in B, B1>,
) : StreamCodec<B, Either<A, B1>> {
    override fun decode(buffer: B): Either<A, B1> = when (buffer.readBoolean()) {
        true -> Either.Left(left.decode(buffer))
        false -> Either.Right(right.decode(buffer))
    }

    override fun encode(buffer: B, value: Either<A, B1>) {
        value.map(
            {
                buffer.writeBoolean(true)
                left.encode(buffer, it)
            },
            {
                buffer.writeBoolean(false)
                right.encode(buffer, it)
            },
        )
    }
}
