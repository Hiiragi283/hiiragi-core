package hiiragi283.core.api.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.collection.isEmpty
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemView
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.api.storage.item.toResource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

class HTViewRecipeInput(val items: List<HTItemView>, val fluids: List<HTFluidView>, val propertyMap: HTPropertyMap) : RecipeInput {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTViewRecipeInput? = Builder().apply(builderAction).build()
    }

    fun getItemView(index: Int): HTItemView = items[index]

    fun getFluidView(index: Int): HTFluidView = fluids[index]

    //    RecipeInput    //

    @Deprecated("Use `getItemResource(Int)` instead", ReplaceWith("this.getResource(Int)"), DeprecationLevel.ERROR)
    override fun getItem(index: Int): ItemStack = items.getOrNull(index)?.getItemStack() ?: ItemStack.EMPTY

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean {
        val bool1: Boolean = items.isEmpty(HTItemView::isEmpty)
        val bool2: Boolean = fluids.isEmpty(HTFluidView::isEmpty)
        val bool3: Boolean = propertyMap.isEmpty()
        return bool1 && bool2 && bool3
    }

    //    Builder    //

    class Builder : HTPropertyMap.Mutable by HTBasicPropertyMap.Mutable() {
        var items: MutableList<HTItemView> = mutableListOf()
        var fluids: MutableList<HTFluidView> = mutableListOf()

        fun fakeView(stack: ItemStack): HTItemView = object : HTItemView {
            override fun getResource(): HTItemResourceType? = stack.toResource()

            override fun getCapacity(resource: HTItemResourceType?): Int = stack.maxStackSize

            override fun getAmount(): Int = stack.count
        }

        fun fakeView(stack: FluidStack): HTFluidView = object : HTFluidView {
            override fun getResource(): HTFluidResourceType? = stack.toResource()

            override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

            override fun getAmount(): Int = stack.amount
        }

        fun build(): HTViewRecipeInput? = HTViewRecipeInput(items, fluids, this).takeUnless(HTViewRecipeInput::isEmpty)
    }
}
