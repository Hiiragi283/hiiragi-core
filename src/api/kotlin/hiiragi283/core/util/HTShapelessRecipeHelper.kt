package hiiragi283.core.util

import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResourcePair
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResourcePair
import hiiragi283.core.api.storage.resource.HTResourceType
import hiiragi283.core.api.storage.resource.HTResourceView
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
object HTShapelessRecipeHelper {
    //    Input    //

    /**
     * @since 0.10.0
     */
    @JvmName("createMapFromViews")
    @JvmStatic
    fun <T : HTResourceType> createMap(views: Iterable<HTResourceView<T>>): Map<T, Int> = views.fold(hashMapOf()) { map: HashMap<T, Int>, view: HTResourceView<T> ->
        val resource: T = view.getResource() ?: return@fold map
        map[resource] = (map[resource] ?: 0) + view.getAmount()
        map
    }

    /**
     * @since 0.10.0
     */
    @JvmName("createMapFromItems")
    @JvmStatic
    fun createMap(stacks: Iterable<ItemStack>): Map<HTItemResourceType, Int> = stacks.fold(hashMapOf()) { map: HashMap<HTItemResourceType, Int>, stack: ItemStack ->
        val (resource: HTItemResourceType, amount: Int) = stack.toResourcePair() ?: return@fold map
        map[resource] = (map[resource] ?: 0) + amount
        map
    }

    /**
     * @since 0.15.2
     */
    @JvmName("mergeItemStacks")
    @JvmStatic
    fun mergeStacks(stacks: Iterable<ItemStack>): List<ItemStack> = createMap(stacks).map { (resource: HTItemResourceType, count: Int) -> resource.toStack(count) }

    /**
     * @since 0.10.0
     */
    @JvmName("createMapFromFluids")
    @JvmStatic
    fun createMap(stacks: Iterable<FluidStack>): Map<HTFluidResourceType, Int> = stacks.fold(hashMapOf()) { map: HashMap<HTFluidResourceType, Int>, stack: FluidStack ->
        val (resource: HTFluidResourceType, amount: Int) = stack.toResourcePair() ?: return@fold map
        map[resource] = (map[resource] ?: 0) + amount
        map
    }

    /**
     * @since 0.15.2
     */
    @JvmName("mergeFluidStacks")
    @JvmStatic
    fun mergeStacks(stacks: Iterable<FluidStack>): List<FluidStack> = createMap(stacks).map { (resource: HTFluidResourceType, amount: Int) -> resource.toStack(amount) }
}
