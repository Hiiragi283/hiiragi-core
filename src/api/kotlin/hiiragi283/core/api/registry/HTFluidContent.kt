package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidType

/**
 * 液体とそれに関する要素を束ねるクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
class HTFluidContent(
    // Required
    val typeHolder: HTHolderLike<FluidType, *>,
    private val sourceHolder: HTFluidHolderLike<out Fluid>,
    private val bucketHolder: HTItemHolderLike<*>,
    val fluidTag: TagKey<Fluid>,
    val bucketTag: TagKey<Item>,
    // Optional
    val flowingHolder: HTHolderLike<Fluid, out FlowingFluid>?,
    val blockHolder: HTBlockHolderLike<out LiquidBlock>?,
) : HTSimpleHolderLikeDelegate<Fluid>,
    HTFluidHolderLike<Fluid> {
    override fun getFluidHolder(): Holder<Fluid> = getHolder()

    override fun asFluid(): Fluid = get()

    override fun getBucket(): Item = getBucketHolder().asItem()

    override fun getBucketHolder(): HTItemHolderLike<*> = bucketHolder

    override fun getFluidType(): FluidType = typeHolder.get()

    override fun get(): Fluid = sourceHolder.asFluid()

    override fun getHolder(): Holder<Fluid> = sourceHolder.getFluidHolder()
}
