package hiiragi283.core.common.material

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.toId
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem

object ColoredMaterials {
    @JvmField
    val BANNER: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_banner")

    @JvmField
    val BED: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_bed")

    @JvmField
    val CANDLE: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_candle")

    @JvmField
    val CARPET: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_carpet")

    @JvmField
    val CONCRETE: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_concrete")

    @JvmField
    val CONCRETE_POWDER: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_concrete_powder")

    @JvmField
    val DYE: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_dye")

    @JvmField
    val GLAZED_TERRACOTTA: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_glazed_terracotta")

    @JvmField
    val SHULKER_BOX: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_shulker_box")

    @JvmField
    val STAINED_GLASS: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_stained_glass")

    @JvmField
    val STAINED_GLASS_PANE: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_stained_glass_pane")

    @JvmField
    val TERRACOTTA: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_terracotta")

    @JvmField
    val WOOL: Map<HTDefaultColor, HTItemHolderLike<Item>> = create("%s_wool")

    @JvmStatic
    private fun create(path: String): Map<HTDefaultColor, HTItemHolderLike<Item>> = HTDefaultColor.entries.associateWith {
        DeferredItem.createItem<Item>(HTConst.MINECRAFT.toId(path.replace("%s", it.serializedName))).toLike()
    }
}
