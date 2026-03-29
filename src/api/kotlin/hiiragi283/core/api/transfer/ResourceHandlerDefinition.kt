package hiiragi283.core.api.transfer

import net.neoforged.neoforge.transfer.resource.Resource

interface ResourceHandlerDefinition<T : Resource> {
    val size: Int

    fun getCapacity(index: Int, resource: T): Int

    fun isValid(index: Int, resource: T): Boolean

    fun canInsert(index: Int, access: HTHandlerAccess): Boolean

    fun canExtract(index: Int, access: HTHandlerAccess): Boolean
}
