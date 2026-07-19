package hiiragi283.core.api.recipe

import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResourcePair
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResourcePair
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
data object HTRecipeResultHelper {
    /**
     * 複数の[ItemStack]を同じ種類同士で合併します。
     */
    @JvmName("mergeItemStacks")
    @JvmStatic
    fun mergeStacks(stacks: Iterable<ItemStack>): Set<ItemStack> {
        val map: MutableMap<HTItemResourceType, Int> = hashMapOf()
        for (stack: ItemStack in stacks) {
            val (resource: HTItemResourceType, amount: Int) = stack.toResourcePair() ?: continue
            map[resource] = (map[resource] ?: 0) + amount
        }
        return map.mapTo(mutableSetOf()) { (resource: HTItemResourceType, count: Int) -> resource.toStack(count) }
    }

    /**
     * 複数の[FluidStack]を同じ種類同士で合併します。
     */
    @JvmName("mergeFluidStacks")
    @JvmStatic
    fun mergeStacks(stacks: Iterable<FluidStack>): Set<FluidStack> {
        val map: MutableMap<HTFluidResourceType, Int> = hashMapOf()
        for (stack: FluidStack in stacks) {
            val (resource: HTFluidResourceType, amount: Int) = stack.toResourcePair() ?: continue
            map[resource] = (map[resource] ?: 0) + amount
        }
        return map.mapTo(mutableSetOf()) { (resource: HTFluidResourceType, count: Int) -> resource.toStack(count) }
    }
}
