package hiiragi283.core.api.registry

import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags

/**
 * バニラで追加される液体向けの[HTFluidContent]の一覧をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
@Suppress("DEPRECATION")
data object VanillaFluidContents {
    @JvmField
    val WATER = HTFluidContent(
        NeoForgeMod.WATER_TYPE.toLike(),
        HTFluidHolderLike.of(Fluids.WATER),
        HTItemHolderLike.of(Items.WATER_BUCKET),
        Tags.Fluids.WATER,
        Tags.Items.BUCKETS_WATER,
        null,
        null,
    )

    @JvmField
    val LAVA = HTFluidContent(
        NeoForgeMod.LAVA_TYPE.toLike(),
        HTFluidHolderLike.of(Fluids.LAVA),
        HTItemHolderLike.of(Items.LAVA_BUCKET),
        Tags.Fluids.LAVA,
        Tags.Items.BUCKETS_LAVA,
        null,
        null,
    )

    @JvmField
    val MILK = HTFluidContent(
        NeoForgeMod.MILK_TYPE.toLike(),
        HTFluidHolderLike.of(NeoForgeMod.MILK),
        HTItemHolderLike.of(Items.MILK_BUCKET),
        Tags.Fluids.MILK,
        Tags.Items.BUCKETS_MILK,
        null,
        null,
    )
}
