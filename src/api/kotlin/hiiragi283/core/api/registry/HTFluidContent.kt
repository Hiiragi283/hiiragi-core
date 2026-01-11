package hiiragi283.core.api.registry

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * 液体とそれに関する要素を束ねるクラスです。
 * @param TYPE [FluidType]のクラス
 * @param FLUID 液体のクラス
 * @param ITEM バケツのクラス
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 * @see mekanism.common.registration.impl.FluidRegistryObject
 */
open class HTFluidContent<TYPE : FluidType, FLUID : Fluid, ITEM : Item>(
    val typeHolder: HTHolderLike<FluidType, TYPE>,
    fluidHolder: HTHolderLike.HolderDelegate<Fluid, FLUID>,
    val fluidTag: TagKey<Fluid>,
    val bucketHolder: HTItemHolderLike<ITEM>,
    val bucketTag: TagKey<Item>,
) : HTHolderLike.HolderDelegate<Fluid, FLUID> by fluidHolder {
    /**
     * 保持している液体の[net.neoforged.neoforge.fluids.FluidType]を取得します。
     */
    fun getFluidType(): TYPE = typeHolder.get()

    /**
     * 保持しているバケツを取得します。
     */
    fun getBucket(): ITEM = bucketHolder.asItem()

    fun toStack(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): FluidStack = FluidStack(get(), amount)

    fun toResource(patch: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResourceType = HTFluidResourceType.of(get(), patch)

    //    Flowing    //

    /**
     * 液体流とブロックに対応した[HTFluidContent]の拡張クラスです。
     * @param STILL 液体源のクラス
     * @param FLOWING 液体流のクラス
     * @author Hiiragi Tsubasa
     * @since 0.6.0
     */
    class Flowing<TYPE : FluidType, STILL : Fluid, FLOWING : Fluid, ITEM : Item>(
        typeHolder: HTHolderLike<FluidType, TYPE>,
        fluidHolder: HTHolderLike.HolderDelegate<Fluid, STILL>,
        val flowingHolder: HTHolderLike<Fluid, FLOWING>,
        fluidTag: TagKey<Fluid>,
        val blockHolder: HTHolderLike<Block, out LiquidBlock>,
        bucketHolder: HTItemHolderLike<ITEM>,
        bucketTag: TagKey<Item>,
    ) : HTFluidContent<TYPE, STILL, ITEM>(typeHolder, fluidHolder, fluidTag, bucketHolder, bucketTag)
}
