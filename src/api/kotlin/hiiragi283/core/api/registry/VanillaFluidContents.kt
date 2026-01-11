package hiiragi283.core.api.registry

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

@Suppress("DEPRECATION")
object VanillaFluidContents {
    @JvmField
    val WATER: HTFluidContent<FluidType, Fluid, Item> = Impl(
        NeoForgeMod.WATER_TYPE.toLike(),
        Fluids.WATER.toHolderLike(),
        Tags.Fluids.WATER,
        Items.WATER_BUCKET.toHolderLike(),
        Tags.Items.BUCKETS_WATER,
    )

    @JvmField
    val LAVA: HTFluidContent<FluidType, Fluid, Item> = Impl(
        NeoForgeMod.LAVA_TYPE.toLike(),
        Fluids.LAVA.toHolderLike(),
        Tags.Fluids.LAVA,
        Items.LAVA_BUCKET.toHolderLike(),
        Tags.Items.BUCKETS_LAVA,
    )

    @JvmField
    val MILK: HTFluidContent<FluidType, Fluid, Item> = Impl(
        NeoForgeMod.MILK_TYPE.toLike(),
        NeoForgeMod.MILK.toLike(),
        Tags.Fluids.MILK,
        Items.MILK_BUCKET.toHolderLike(),
        Tags.Items.BUCKETS_MILK,
    )

    private class Impl(
        override val typeHolder: HTHolderLike<FluidType, FluidType>,
        fluidHolder: HTHolderLike.HolderDelegate<Fluid, Fluid>,
        override val fluidTag: TagKey<Fluid>,
        override val bucketHolder: HTItemHolderLike<Item>,
        override val bucketTag: TagKey<Item>,
    ) : HTFluidContent<FluidType, Fluid, Item>,
        HTHolderLike.HolderDelegate<Fluid, Fluid> by fluidHolder
}
