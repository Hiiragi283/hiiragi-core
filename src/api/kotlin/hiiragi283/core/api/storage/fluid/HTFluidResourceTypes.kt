package hiiragi283.core.api.storage.fluid

import net.neoforged.neoforge.fluids.FluidStack

fun FluidStack.toResource(): HTFluidResourceType? = HTFluidResourceType.of(this)
