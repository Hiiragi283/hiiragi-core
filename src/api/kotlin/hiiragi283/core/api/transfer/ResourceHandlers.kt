package hiiragi283.core.api.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.resource.Resource

typealias FluidResourceHandler = ResourceHandler<FluidResource>
typealias ItemResourceHandler = ResourceHandler<ItemResource>

val ResourceHandler<*>.indices: IntRange get() = (0..<size())

fun <T : Resource> ResourceHandler<T>.asResourceSequence(): Sequence<T> = this.indices.asSequence().map(this::getResource)
