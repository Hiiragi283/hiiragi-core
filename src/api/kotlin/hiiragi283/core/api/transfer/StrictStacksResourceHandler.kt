package hiiragi283.core.api.transfer

import com.mojang.serialization.Codec
import net.minecraft.core.NonNullList
import net.neoforged.neoforge.transfer.StacksResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

abstract class StrictStacksResourceHandler<S : Any, T : Resource> :
    StacksResourceHandler<S, T>,
    StrictResourceHandler<T> {
    protected val definition: ResourceHandlerDefinition<T>

    constructor(definition: ResourceHandlerDefinition<T>, emptyStack: S, stackCodec: Codec<S>) : super(
        definition.size,
        emptyStack,
        stackCodec,
    ) {
        this.definition = definition
    }

    constructor(
        stacks: NonNullList<S>,
        definition: ResourceHandlerDefinition<T>,
        emptyStack: S,
        stackCodec: Codec<S>,
    ) : super(stacks, emptyStack, stackCodec) {
        this.definition = definition
    }

    override fun insert(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int = when {
        definition.canInsert(index, access) -> super<StacksResourceHandler>.insert(index, resource, amount, transaction)
        else -> 0
    }

    override fun extract(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int = when {
        definition.canExtract(index, access) -> super<StacksResourceHandler>.extract(index, resource, amount, transaction)
        else -> 0
    }

    //    StacksResourceHandler    //

    override fun isValid(index: Int, resource: T): Boolean = definition.isValid(index, resource)

    override fun getCapacity(index: Int, resource: T): Int = definition.getCapacity(index, resource)

    final override fun insert(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = super<StacksResourceHandler>.insert(index, resource, amount, transaction)

    final override fun extract(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = super<StacksResourceHandler>.extract(index, resource, amount, transaction)
}
