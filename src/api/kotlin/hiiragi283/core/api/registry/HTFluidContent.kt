package hiiragi283.core.api.registry

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

interface HTFluidContent<TYPE : FluidType, FLUID : Fluid, ITEM : Item> :
    HTHolderLike<Fluid, FLUID>,
    HTKeyLike.HolderDelegate<Fluid> {
    val typeHolder: HTDeferredHolder<FluidType, TYPE>
    val fluidHolder: HTDeferredHolder<Fluid, FLUID>
    val fluidTag: TagKey<Fluid>
    val bucketHolder: HTItemHolderLike<ITEM>
    val bucketTag: TagKey<Item>

    fun getFluidType(): TYPE = typeHolder.get()

    fun getBucket(): ITEM = bucketHolder.get()

    fun isOf(fluid: Fluid): Boolean = get() == fluid

    fun isOf(tagKey: TagKey<Fluid>): Boolean = getHolder().`is`(tagKey) || tagKey == fluidTag

    fun isOf(stack: FluidStack): Boolean = stack.`is`(get()) || stack.`is`(fluidTag)

    fun isOf(resource: HTFluidResourceType): Boolean = resource.isOf(get()) || resource.isOf(fluidTag)

    fun toStack(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): FluidStack = FluidStack(get(), amount)

    fun toResource(patch: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResourceType = HTFluidResourceType.of(get(), patch)

    override fun get(): FLUID = fluidHolder.get()

    override fun getHolder(): Holder<Fluid> = fluidHolder.delegate

    //    Flowing    //

    interface Flowing<TYPE : FluidType, STILL : Fluid, FLOWING : Fluid, ITEM : Item> : HTFluidContent<TYPE, STILL, ITEM> {
        val flowingHolder: HTDeferredHolder<Fluid, FLOWING>
        val blockHolder: HTDeferredHolder<Block, out LiquidBlock>
    }
}
