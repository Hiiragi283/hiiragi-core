package hiiragi283.core.api.material

import com.google.common.base.Suppliers
import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTSimpleHolderLike
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import java.util.function.Supplier

interface HTMaterialContents<R : Any, V : Any> : HTTable<R, HTMaterialKey, HTMaterialContents.Entry<V>> {
    companion object {
        @JvmStatic
        fun blockEntry(key: ResourceKey<Block>, block: Block, isBuiltIn: Boolean): Entry<Block> =
            Entry(key, Suppliers.ofInstance(block), isBuiltIn)

        @JvmStatic
        fun fluidEntry(key: ResourceKey<Fluid>, fluid: Fluid, isBuiltIn: Boolean): Entry<Fluid> =
            Entry(key, Suppliers.ofInstance(fluid), isBuiltIn)

        @JvmStatic
        fun itemEntry(key: ResourceKey<Item>, item: Item, isBuiltIn: Boolean): Entry<Item> =
            Entry(key, Suppliers.ofInstance(item), isBuiltIn)
    }

    operator fun get(row: R, material: HTMaterialLike): Entry<V>? = this[row, material.asMaterialKey()]

    fun getOrThrow(row: R, material: HTMaterialLike): Entry<V> = get(row, material) ?: error(getErrorMessage(row, material.asMaterialKey()))

    fun getErrorMessage(row: R, material: HTMaterialKey): String

    fun column(material: HTMaterialLike): Map<R, Entry<V>> = this.column(material.asMaterialKey())

    class Entry<V : Any>(private val holder: HTHolderLike<V, *>, private val isBuiltIn: Boolean) : HTSimpleHolderLike<V> {
        constructor(key: ResourceKey<V>, value: Supplier<out V>, isBuiltIn: Boolean) : this(
            object : HTSimpleHolderLike<V> {
                override fun getResourceKey(): ResourceKey<V> = key

                override fun get(): V = value.get()
            },
            isBuiltIn,
        )

        override fun getResourceKey(): ResourceKey<V> = holder.getResourceKey()

        override fun get(): V = holder.get()

        operator fun component1(): ResourceKey<V> = getResourceKey()

        operator fun component2(): V = get()

        operator fun component3(): Boolean = isBuiltIn
    }
}
