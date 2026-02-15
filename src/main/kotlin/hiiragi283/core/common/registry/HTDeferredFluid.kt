package hiiragi283.core.common.registry

import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTFluidHolderLike
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid

class HTDeferredFluid<FLUID : Fluid> :
    HTDeferredHolder<Fluid, FLUID>,
    HTFluidHolderLike.Simple<FLUID> {
    constructor(key: ResourceKey<Fluid>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.FLUID, id)

    override fun getId(): ResourceLocation = super<HTDeferredHolder>.getId()

    override fun asFluid(): FLUID = get()

    override fun getFluidHolder(): Holder<Fluid> = getHolder()
}
