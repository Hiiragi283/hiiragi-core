package hiiragi283.core.api.registry

import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

/**
 * バニラで追加される液体向けの[HTFluidContent]の一覧をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
@Suppress("DEPRECATION")
object VanillaFluidContents {
    @JvmField
    val WATER: HTFluidContent<FluidType, FlowingFluid, Item> = HTFluidContent(
        NeoForgeMod.WATER_TYPE.toLike(),
        Fluids.WATER.toHolderLike(),
        Tags.Fluids.WATER,
        Items.WATER_BUCKET.toHolderLike(),
        Tags.Items.BUCKETS_WATER,
    )

    @JvmField
    val LAVA: HTFluidContent<FluidType, Fluid, Item> = HTFluidContent(
        NeoForgeMod.LAVA_TYPE.toLike(),
        Fluids.LAVA.toHolderLike(),
        Tags.Fluids.LAVA,
        Items.LAVA_BUCKET.toHolderLike(),
        Tags.Items.BUCKETS_LAVA,
    )

    @JvmField
    val MILK: HTFluidContent<FluidType, Fluid, Item> = HTFluidContent(
        NeoForgeMod.MILK_TYPE.toLike(),
        NeoForgeMod.MILK.toLike(),
        Tags.Fluids.MILK,
        Items.MILK_BUCKET.toHolderLike(),
        Tags.Items.BUCKETS_MILK,
    )
}
