package hiiragi283.core.common.util

import hiiragi283.core.api.transfer.fluid.toResourcePair
import hiiragi283.core.api.transfer.indices
import hiiragi283.core.api.transfer.item.toResourcePair
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.resource.Resource

/**
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
object HTShapelessRecipeHelper {
    //    Input    //

    @JvmName("createMapFromHandler")
    fun <T : Resource> createMap(handler: ResourceHandler<T>): Map<T, Int> =
        handler.indices.fold(hashMapOf()) { map: MutableMap<T, Int>, index: Int ->
            val resource: T = handler.getResource(index)
            if (!resource.isEmpty) {
                val amount: Int = handler.getAmountAsInt(index)
                map[resource] = (map[resource] ?: 0) + amount
            }
            map
        }

    /**
     * @since 0.10.0
     */
    @JvmName("createMapFromItems")
    @JvmStatic
    fun createMap(stacks: Iterable<ItemStack>): Map<ItemResource, Int> =
        stacks.fold(hashMapOf()) { map: HashMap<ItemResource, Int>, stack: ItemStack ->
            val (resource: ItemResource, amount: Int) = stack.toResourcePair()
            if (!resource.isEmpty) {
                map[resource] = (map[resource] ?: 0) + amount
            }
            map
        }

    /**
     * @since 0.10.0
     */
    @JvmName("createMapFromFluids")
    @JvmStatic
    fun createMap(stacks: Iterable<FluidStack>): Map<FluidResource, Int> =
        stacks.fold(hashMapOf()) { map: HashMap<FluidResource, Int>, stack: FluidStack ->
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            if (!resource.isEmpty) {
                map[resource] = (map[resource] ?: 0) + amount
            }
            map
        }
}
