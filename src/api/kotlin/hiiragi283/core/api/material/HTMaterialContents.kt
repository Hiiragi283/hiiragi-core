package hiiragi283.core.api.material

import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

interface HTMaterialContents<R : Any, V : Any> : HTTable<R, HTMaterialKey, HTMaterialContents.Entry<V>> {
    companion object {
        @JvmStatic
        fun blockEntry(id: ResourceLocation, block: Block, isBuiltIn: Boolean): Entry<Block> =
            Entry(Registries.BLOCK.createKey(id), block, isBuiltIn)

        @JvmStatic
        fun fluidEntry(id: ResourceLocation, fluid: Fluid, isBuiltIn: Boolean): Entry<Fluid> =
            Entry(Registries.FLUID.createKey(id), fluid, isBuiltIn)

        @JvmStatic
        fun itemEntry(id: ResourceLocation, item: Item, isBuiltIn: Boolean): Entry<Item> =
            Entry(Registries.ITEM.createKey(id), item, isBuiltIn)
    }

    operator fun get(row: R, material: HTMaterialLike): Entry<V>? = this[row, material.asMaterialKey()]

    fun getOrThrow(row: R, material: HTMaterialLike): Entry<V> = get(row, material) ?: error(getErrorMessage(row, material.asMaterialKey()))

    fun getErrorMessage(row: R, material: HTMaterialKey): String

    fun column(material: HTMaterialLike): Map<R, Entry<V>> = this.column(material.asMaterialKey())

    data class Entry<V : Any>(val key: ResourceKey<V>, val value: V, val isBuiltIn: Boolean) : HTSimpleHolderLike<V> {
        override fun getResourceKey(): ResourceKey<V> = key

        override fun get(): V = value
    }
}
