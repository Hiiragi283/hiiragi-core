package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

val ResourceHandler<*>.indices: IntRange get() = (0..<size())

//    Fluid    //

typealias FluidResourceHandler = ResourceHandler<FluidResource>

typealias HTFluidResourceHandler = HTResourceHandler<FluidResource>

//    Item    //

typealias ItemResourceHandler = ResourceHandler<ItemResource>

typealias HTItemResourceHandler = HTResourceHandler<ItemResource>
