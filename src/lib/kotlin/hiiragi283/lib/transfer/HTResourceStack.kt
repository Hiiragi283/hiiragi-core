package hiiragi283.lib.transfer

import com.google.common.primitives.Ints
import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import net.neoforged.neoforge.transfer.resource.Resource

@ConsistentCopyVisibility
@JvmRecord
data class HTResourceStack<RESOURCE : Resource> private constructor(val resource: RESOURCE, val amountAsLong: Long) {
    companion object {
        @JvmStatic
        fun <RESOURCE : Resource> codec(resource: Codec<RESOURCE>): Codec<HTResourceStack<RESOURCE>> = HTCodecs.record { instance ->
            instance.group(
                resource.fieldOf("resource").forGetter(HTResourceStack<RESOURCE>::resource),
                HTCodecs.NON_NEGATIVE_LONG.fieldOf(HTConstants.AMOUNT).forGetter(HTResourceStack<RESOURCE>::amountAsLong),
            ).apply(instance, ::HTResourceStack)
        }

        @JvmStatic
        fun <RESOURCE : Resource> of(resource: RESOURCE, amount: Long): HTResourceStack<RESOURCE>? = when {
            isEmpty(resource, amount) -> null
            else -> HTResourceStack(resource, amount)
        }

        @JvmStatic
        operator fun <RESOURCE : Resource> invoke(resource: RESOURCE, amount: Long): HTResourceStack<RESOURCE>? = of(resource, amount)

        @JvmStatic
        fun isEmpty(resource: Resource, amount: Long): Boolean = resource.isEmpty || amount <= 0
    }

    val amountAsInt: Int get() = Ints.saturatedCast(amountAsLong)

    fun isEmpty(): Boolean = isEmpty(resource, amountAsLong)

    inline fun <T> mapAsLong(transform: (RESOURCE, Long) -> T): T = transform(resource, amountAsLong)

    inline fun <T> mapAsInt(transform: (RESOURCE, Int) -> T): T = transform(resource, amountAsInt)
}
