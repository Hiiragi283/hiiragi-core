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
        Fluids.WATER.toLike(),
        Items.WATER_BUCKET.toLike(),
        Tags.Fluids.WATER,
        Tags.Items.BUCKETS_WATER,
        null,
        null,
    )

    @JvmField
    val LAVA = HTFluidContent(
        NeoForgeMod.LAVA_TYPE.toLike(),
        Fluids.LAVA.toLike(),
        Items.LAVA_BUCKET.toLike(),
        Tags.Fluids.LAVA,
        Tags.Items.BUCKETS_LAVA,
        null,
        null,
    )

    @JvmField
    val MILK = HTFluidContent(
        NeoForgeMod.MILK_TYPE.toLike(),
        NeoForgeMod.MILK.toLike(),
        Items.MILK_BUCKET.toLike(),
        Tags.Fluids.MILK,
        Tags.Items.BUCKETS_MILK,
        null,
        null,
    )
}
