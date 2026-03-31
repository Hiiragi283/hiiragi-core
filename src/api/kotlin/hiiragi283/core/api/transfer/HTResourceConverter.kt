package hiiragi283.core.api.transfer

import net.neoforged.neoforge.transfer.resource.Resource

interface HTResourceConverter<S : Any, T : Resource> {
    fun getEmptyStack(): S

    fun getResource(stack: S): T

    fun getAmount(stack: S): Long

    fun setAmount(stack: S, amount: Long)

    fun copyStack(stack: S): S

    fun createStack(resource: T, amount: Int): S
}
