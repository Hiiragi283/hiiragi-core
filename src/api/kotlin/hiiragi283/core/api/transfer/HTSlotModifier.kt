package hiiragi283.core.api.transfer

import hiiragi283.core.api.transfer.fluid.toResourcePair
import hiiragi283.core.api.transfer.item.toResourcePair
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.resource.Resource

/**
 * @see net.neoforged.neoforge.transfer.IndexModifier
 */
fun interface HTSlotModifier<T : Resource> {
    operator fun set(resource: T, amount: Int)
}

//    Extension    //

fun HTSlotModifier<ItemResource>.set(stack: ItemStack) {
    val (resource: ItemResource, count: Int) = stack.toResourcePair()
    this[resource] = count
}

fun HTSlotModifier<FluidResource>.set(stack: FluidStack) {
    val (resource: FluidResource, count: Int) = stack.toResourcePair()
    this[resource] = count
}
