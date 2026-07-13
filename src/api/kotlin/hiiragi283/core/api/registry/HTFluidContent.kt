package hiiragi283.core.api.registry

import hiiragi283.core.api.fluid.createFluidStack
import hiiragi283.core.api.resource.SupplierWithId
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * 液体とそれに関する要素を束ねるクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
sealed class HTFluidContent(
    val typeHolder: HTDeferredFluidType<FluidType>,
    val sourceHolder: HTDeferredHolder<Fluid, *>,
    val bucketHolder: HTSimpleDeferredItem,
    val fluidTag: TagKey<Fluid>,
    val bucketTag: TagKey<Item>,
) : SupplierWithId<Fluid> by sourceHolder {
    fun getFluidType(): FluidType = typeHolder.get()

    fun toStack(amount: Int = FluidType.BUCKET_VOLUME, patch: DataComponentPatch = DataComponentPatch.EMPTY): FluidStack = createFluidStack(this.get(), amount, patch)

    /**
     * 基本的な[HTFluidContent]の実装クラスです。
     * @since 21.1.0
     */
    class Virtual(
        typeHolder: HTDeferredFluidType<FluidType>,
        sourceHolder: HTDeferredHolder<Fluid, *>,
        bucketHolder: HTSimpleDeferredItem,
        fluidTag: TagKey<Fluid>,
        bucketTag: TagKey<Item>,
    ) : HTFluidContent(typeHolder, sourceHolder, bucketHolder, fluidTag, bucketTag)

    /**
     * [FlowingFluid]に基づいた[HTFluidContent]の実装クラスです。
     * @since 21.1.0
     */
    class Flowing(
        typeHolder: HTDeferredFluidType<FluidType>,
        sourceHolder: HTDeferredHolder<Fluid, FlowingFluid>,
        bucketHolder: HTSimpleDeferredItem,
        fluidTag: TagKey<Fluid>,
        bucketTag: TagKey<Item>,
        val flowingHolder: HTDeferredHolder<Fluid, FlowingFluid>,
        val blockHolder: HTDeferredBlock<LiquidBlock>?,
    ) : HTFluidContent(typeHolder, sourceHolder, bucketHolder, fluidTag, bucketTag)
}
