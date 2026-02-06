package hiiragi283.core.api.recipe.input

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.toResourcePair
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemView
import hiiragi283.core.api.storage.item.toResourcePair
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
class HTViewRecipeInput private constructor(
    val items: Map<HTItemResourceType, Int>,
    val fluids: Map<HTFluidResourceType, Int>,
    val catalyst: HTItemResourceType?,
    val propertyMap: HTPropertyMap,
) : HTFluidRecipeInput {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTViewRecipeInput? = Builder().apply(builderAction).build()
    }

    private val itemList: List<Pair<HTItemResourceType, Int>> = items.toList()
    private val fluidList: List<Pair<HTFluidResourceType, Int>> = fluids.toList()

    //    RecipeInput    //

    override fun getFluid(index: Int): FluidStack {
        val (resource: HTFluidResourceType, amount: Int) = fluidList.getOrNull(index) ?: return FluidStack.EMPTY
        return resource.toStack(amount)
    }

    override fun getFluidSize(): Int = fluids.size

    override fun getItem(index: Int): ItemStack {
        val (resource: HTItemResourceType, count: Int) = itemList.getOrNull(index) ?: return ItemStack.EMPTY
        return resource.toStack(count)
    }

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.isEmpty() && fluids.isEmpty() && catalyst == null && propertyMap.isEmpty()

    //    Builder    //

    class Builder : HTPropertyMap.Mutable by HTBasicPropertyMap.Mutable() {
        private val items: MutableMap<HTItemResourceType, Int> = mutableMapOf()
        private val fluids: MutableMap<HTFluidResourceType, Int> = mutableMapOf()
        var catalyst: HTItemResourceType? = null

        // Item
        @JvmName("addItem")
        operator fun plusAssign(stack: ItemStack) {
            val (resource: HTItemResourceType, count: Int) = stack.toResourcePair() ?: return
            items.compute(resource) { _, old: Int? -> (old ?: 0) + count }
        }

        @JvmName("addItem")
        operator fun plusAssign(view: HTItemView) {
            val resource: HTItemResourceType = view.getResource() ?: return
            items.compute(resource) { _, old: Int? -> (old ?: 0) + view.getAmount() }
        }

        // Fluid
        @JvmName("addFluid")
        operator fun plusAssign(stack: FluidStack) {
            val (resource: HTFluidResourceType, count: Int) = stack.toResourcePair() ?: return
            fluids.compute(resource) { _, old: Int? -> (old ?: 0) + count }
        }

        @JvmName("addFluid")
        operator fun plusAssign(view: HTFluidView) {
            val resource: HTFluidResourceType = view.getResource() ?: return
            fluids.compute(resource) { _, old: Int? -> (old ?: 0) + view.getAmount() }
        }

        fun build(): HTViewRecipeInput? = HTViewRecipeInput(items, fluids, catalyst, this).takeUnless(HTViewRecipeInput::isEmpty)
    }
}
