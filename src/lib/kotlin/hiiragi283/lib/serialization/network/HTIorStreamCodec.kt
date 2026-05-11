package hiiragi283.lib.serialization.network

import hiiragi283.lib.util.Ior
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

/**
 * [Ior]向けの[StreamCodec]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
internal class HTIorStreamCodec<B : ByteBuf, A : Any, B1 : Any>(private val left: StreamCodec<in B, A>, private val right: StreamCodec<in B, B1>) : StreamCodec<B, Ior<A, B1>> {
    override fun decode(buffer: B): Ior<A, B1> = when (buffer.readInt()) {
        1 -> Ior.Left(left.decode(buffer))
        2 -> Ior.Right(right.decode(buffer))
        else -> {
            val leftIn: A = left.decode(buffer)
            val rightIn: B1 = right.decode(buffer)
            Ior.Both(leftIn, rightIn)
        }
    }

    override fun encode(buffer: B, value: Ior<A, B1>) {
        value.fold(
            {
                buffer.writeInt(1)
                left.encode(buffer, it)
            },
            {
                buffer.writeInt(2)
                right.encode(buffer, it)
            },
            { left: A, right: B1 ->
                buffer.writeInt(0)
                this.left.encode(buffer, left)
                this.right.encode(buffer, right)
            },
        )
    }
}
