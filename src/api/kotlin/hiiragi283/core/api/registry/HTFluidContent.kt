package hiiragi283.core.api.registry

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
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
class HTFluidContent(
    // Required
    val typeHolder: HTHolderLike<FluidType, *>,
    val sourceHolder: HTHolderLike.HolderDelegate<Fluid, *>,
    val bucketHolder: HTItemHolderLike<*>,
    val fluidTag: TagKey<Fluid>,
    val bucketTag: TagKey<Item>,
    // Optional
    val flowingHolder: HTHolderLike<Fluid, out FlowingFluid>?,
    val blockHolder: HTHolderLike<Block, out LiquidBlock>?,
) : HTHolderLike.HolderDelegate<Fluid, Fluid> {
    /**
     * 保持している液体の[FluidType]を取得します。
     */
    fun getFluidType(): FluidType = typeHolder.get()

    /**
     * 保持しているバケツを取得します。
     */
    fun getBucket(): Item = bucketHolder.asItem()

    fun toStack(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): FluidStack = FluidStack(get(), amount)

    fun toResource(patch: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResourceType? = this.get().toResource(patch)

    //    HTHolderLike    //

    override fun get(): Fluid = sourceHolder.get()

    override fun getHolder(): Holder<Fluid> = sourceHolder.getHolder()

    override fun getId(): ResourceLocation = sourceHolder.getId()
}
