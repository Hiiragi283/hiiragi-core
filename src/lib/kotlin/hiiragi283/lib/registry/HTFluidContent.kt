package hiiragi283.lib.registry

import net.minecraft.resources.Identifier
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
    val typeHolder: HTDeferredHolder<FluidType, *>,
    private val sourceHolder: HTDeferredHolder<Fluid, *>,
    val bucketHolder: HTSimpleDeferredItem,
    val fluidTag: TagKey<Fluid>,
    val bucketTag: TagKey<Item>,
    // Optional
    val flowingHolder: HTDeferredHolder<Fluid, out FlowingFluid>?,
    val blockHolder: HTDeferredBlock<out LiquidBlock>?,
) : HTFluidHolderLike<Fluid> {
    override fun getBucket(): HTSimpleDeferredItem = bucketHolder

    override fun getFluidType(): FluidType = typeHolder.get()

    override fun get(): Fluid = sourceHolder.get()

    override fun getId(): Identifier = sourceHolder.id
}
