package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
import net.minecraft.core.Holder
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

interface HTFluidWithTag<FLUID : Fluid> :
    HTHolderLike<Fluid, FLUID>,
    HTKeyLike.HolderDelegate<Fluid> {
    companion object {
        @Suppress("DEPRECATION")
        @JvmField
        val WATER: HTFluidWithTag<FlowingFluid> = object : HTFluidWithTag<FlowingFluid> {
            override fun getFluidTag(): TagKey<Fluid> = Tags.Fluids.WATER

            override fun getFluidType(): FluidType = NeoForgeMod.WATER_TYPE.value()

            override fun get(): FlowingFluid = Fluids.WATER

            override fun getHolder(): Holder<Fluid> = get().builtInRegistryHolder()
        }

        @Suppress("DEPRECATION")
        @JvmField
        val LAVA: HTFluidWithTag<FlowingFluid> = object : HTFluidWithTag<FlowingFluid> {
            override fun getFluidTag(): TagKey<Fluid> = Tags.Fluids.LAVA

            override fun getFluidType(): FluidType = NeoForgeMod.LAVA_TYPE.value()

            override fun get(): FlowingFluid = Fluids.LAVA

            override fun getHolder(): Holder<Fluid> = get().builtInRegistryHolder()
        }

        @JvmField
        val MILK: HTFluidWithTag<Fluid> = object : HTFluidWithTag<Fluid> {
            override fun getFluidTag(): TagKey<Fluid> = Tags.Fluids.MILK

            override fun getFluidType(): FluidType = NeoForgeMod.MILK_TYPE.get()

            override fun get(): Fluid = getHolder().value()

            override fun getHolder(): Holder<Fluid> = NeoForgeMod.MILK.delegate
        }
    }

    fun getFluidTag(): TagKey<Fluid>

    fun getFluidType(): FluidType
}
