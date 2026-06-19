package hiiragi283.lib.recipe.result

import hiiragi283.lib.transfer.fluid.toResourcePair
import hiiragi283.lib.transfer.item.toResourcePair
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * スタックの合併を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTResultHelper {
    /**
     * [ItemStack]の一覧を[ItemResource]と個数の[Map]に変換します。
     */
    @JvmName("createMapFromItems")
    @JvmStatic
    fun createMap(stacks: Iterable<ItemStack>): Map<ItemResource, Int> {
        val map: MutableMap<ItemResource, Int> = mutableMapOf()
        for (stack: ItemStack in stacks) {
            if (stack.isEmpty) continue
            val (resource: ItemResource, amount: Int) = stack.toResourcePair()
            map[resource] = (map[resource] ?: 0) + amount
        }
        return map
    }

    /**
     * [ItemStack]をまとめます。
     */
    @JvmName("mergeItemStacks")
    @JvmStatic
    fun mergeStacks(stacks: Iterable<ItemStack>): List<ItemStack> = createMap(stacks).map { (resource: ItemResource, count: Int) -> resource.toStack(count) }

    /**
     * [FluidStack]の一覧を[FluidResource]と量の[Map]に変換します。
     */
    @JvmName("createMapFromFluids")
    @JvmStatic
    fun createMap(stacks: Iterable<FluidStack>): Map<FluidResource, Int> {
        val map: MutableMap<FluidResource, Int> = mutableMapOf()
        for (stack: FluidStack in stacks) {
            if (stack.isEmpty) continue
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            map[resource] = (map[resource] ?: 0) + amount
        }
        return map
    }

    /**
     * [FluidStack]をまとめます。
     */
    @JvmName("mergeFluidStacks")
    @JvmStatic
    fun mergeStacks(stacks: Iterable<FluidStack>): List<FluidStack> = createMap(stacks).map { (resource: FluidResource, amount: Int) -> resource.toStack(amount) }
}
