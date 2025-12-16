package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidType

@JvmRecord
data class HTFluidContent<TYPE : FluidType, STILL : Fluid, FLOWING : Fluid>(
    val typeHolder: HTDeferredHolder<FluidType, TYPE>,
    val stillHolder: HTDeferredHolder<Fluid, STILL>,
    val flowingHolder: HTDeferredHolder<Fluid, FLOWING>,
    val fluidTag: TagKey<Fluid>,
    val block: HTDeferredHolder<Block, *>,
    val bucket: HTItemHolderLike<*>,
    val bucketTag: TagKey<Item>,
) : HTFluidWithTag<STILL> {
    override fun getFluidTag(): TagKey<Fluid> = fluidTag

    override fun get(): STILL = stillHolder.get()

    override fun getHolder(): Holder<Fluid> = stillHolder.delegate
}
