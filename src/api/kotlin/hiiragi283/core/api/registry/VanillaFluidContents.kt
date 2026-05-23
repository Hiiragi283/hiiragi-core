package hiiragi283.core.api.registry

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.resource.toId
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

/**
 * バニラで追加される液体向けの[HTFluidContent]の一覧をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
data object VanillaFluidContents {
    @JvmField
    val WATER: HTFluidContent.Virtual = create("water", Tags.Fluids.WATER, Tags.Items.BUCKETS_WATER)

    @JvmField
    val LAVA: HTFluidContent.Virtual = create("lava", Tags.Fluids.LAVA, Tags.Items.BUCKETS_LAVA)

    @JvmField
    val MILK: HTFluidContent.Virtual = create("milk", Tags.Fluids.MILK, Tags.Items.BUCKETS_MILK)

    @JvmStatic
    private fun create(name: String, fluidTag: TagKey<Fluid>, bucketTag: TagKey<Item>): HTFluidContent.Virtual = HTFluidContent.Virtual(
        HTDeferredFluidType(HTConst.MINECRAFT.toId(name)),
        HTDeferredHolder(Registries.FLUID, HTConst.MINECRAFT.toId(name)),
        HTDeferredItem(HTConst.MINECRAFT.toId("${name}_bucket")),
        fluidTag,
        bucketTag,
    )
}
