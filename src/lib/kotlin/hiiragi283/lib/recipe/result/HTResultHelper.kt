package hiiragi283.lib.recipe.result

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

data object HTResultHelper {
    /**
     * @since 0.10.0
     */
    @JvmName("createMapFromItems")
    @JvmStatic
    fun createMap(stacks: Iterable<ItemStack>): Map<ItemResource, Int> = stacks.fold(hashMapOf()) { map: HashMap<ItemResource, Int>, stack: ItemStack ->
        val resource: ItemResource = ItemResource.of(stack)
        if (resource.isEmpty) return@fold map
        map[resource] = (map[resource] ?: 0) + stack.count
        map
    }

    /**
     * @since 0.15.2
     */
    @JvmName("mergeItemStacks")
    @JvmStatic
    fun mergeStacks(stacks: Iterable<ItemStack>): List<ItemStack> = createMap(stacks).map { (resource: ItemResource, count: Int) -> resource.toStack(count) }

    /**
     * @since 0.10.0
     */
    @JvmName("createMapFromItems")
    @JvmStatic
    fun createMap(stacks: Iterable<FluidStack>): Map<FluidResource, Int> = stacks.fold(hashMapOf()) { map: HashMap<FluidResource, Int>, stack: FluidStack ->
        val resource: FluidResource = FluidResource.of(stack)
        if (resource.isEmpty) return@fold map
        map[resource] = (map[resource] ?: 0) + stack.amount
        map
    }

    /**
     * @since 0.15.2
     */
    @JvmName("mergeFluidStacks")
    @JvmStatic
    fun mergeStacks(stacks: Iterable<FluidStack>): List<FluidStack> = createMap(stacks).map { (resource: FluidResource, amount: Int) -> resource.toStack(amount) }
}
