package hiiragi283.core.api.stack

import hiiragi283.core.api.resource.HTKeyLike
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.IWithData

interface ImmutableRegistryStack<TYPE : Any, STACK : ImmutableRegistryStack<TYPE, STACK>> :
    ImmutableStack<TYPE, STACK>,
    HTKeyLike.HolderDelegate<TYPE>,
    IWithData<TYPE> {
    fun isOf(type: TYPE): Boolean = getType() == type

    fun isOf(key: ResourceKey<TYPE>): Boolean = getHolder().`is`(key)

    fun isOf(tagKey: TagKey<TYPE>): Boolean = getHolder().`is`(tagKey)

    @Suppress("DEPRECATION")
    fun isOf(holder: Holder<TYPE>): Boolean = getHolder().`is`(holder)

    fun isOf(holderSet: HolderSet<TYPE>): Boolean = holderSet.contains(getHolder())

    override fun <T : Any> getData(type: DataMapType<TYPE, T>): T? = getHolder().getData(type)
}
