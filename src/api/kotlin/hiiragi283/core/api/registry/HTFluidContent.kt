package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType

typealias HTSimpleFluidContent = HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing>

/**
 * 登録した液体とそれに関する要素を束ねるクラスです。
 * @param TYPE [FluidType]のクラス
 * @param STILL 液体源のクラス
 * @param FLOWING 液体流のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.registration.impl.FluidRegistryObject
 */
@JvmRecord
data class HTFluidContent<TYPE : FluidType, STILL : Fluid, FLOWING : Fluid>(
    val typeHolder: HTDeferredHolder<FluidType, TYPE>,
    val stillHolder: HTDeferredHolder<Fluid, STILL>,
    val flowingHolder: HTDeferredHolder<Fluid, FLOWING>,
    private val fluidTag: TagKey<Fluid>,
    val block: HTDeferredHolder<Block, out LiquidBlock>,
    val bucket: HTItemHolderLike<*>,
    val bucketTag: TagKey<Item>,
) : HTFluidWithTag<STILL> {
    override fun getFluidTag(): TagKey<Fluid> = fluidTag

    override fun getFluidType(): TYPE = typeHolder.get()

    override fun get(): STILL = stillHolder.get()

    override fun getHolder(): Holder<Fluid> = stillHolder.delegate
}
