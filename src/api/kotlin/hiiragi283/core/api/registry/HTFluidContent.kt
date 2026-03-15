package hiiragi283.core.api.registry

import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
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
    private val sourceHolder: HTFluidHolderLike<*>,
    val bucketHolder: HTSimpleItemHolderLike,
    val fluidTag: TagKey<Fluid>,
    val bucketTag: TagKey<Item>,
    // Optional
    val flowingHolder: HTFluidHolderLike<out FlowingFluid>?,
    val blockHolder: HTBlockHolderLike<out LiquidBlock>?,
) : HTSimpleFluidHolderLike {
    override fun getBucket(): HTSimpleItemHolderLike = bucketHolder

    override fun getFluidType(): FluidType = typeHolder.get()

    override fun unwrap(): Either<ResourceKey<Fluid>, Holder<Fluid>> = sourceHolder.unwrap()

    override fun get(): Fluid = sourceHolder.get()
}
