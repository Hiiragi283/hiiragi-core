package hiiragi283.core.common.material

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.resource.toId

object ColoredMaterials {
    @JvmField
    val BANNER: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_banner")

    @JvmField
    val BED: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_bed")

    @JvmField
    val CANDLE: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_candle")

    @JvmField
    val CARPET: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_carpet")

    @JvmField
    val CONCRETE: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_concrete")

    @JvmField
    val CONCRETE_POWDER: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_concrete_powder")

    @JvmField
    val DYE: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_dye")

    @JvmField
    val GLAZED_TERRACOTTA: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_glazed_terracotta")

    @JvmField
    val SHULKER_BOX: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_shulker_box")

    @JvmField
    val STAINED_GLASS: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_stained_glass")

    @JvmField
    val STAINED_GLASS_PANE: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_stained_glass_pane")

    @JvmField
    val TERRACOTTA: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_terracotta")

    @JvmField
    val WOOL: Map<HTDefaultColor, HTSimpleItemHolderLike> = create("%s_wool")

    @JvmStatic
    private fun create(path: String): Map<HTDefaultColor, HTSimpleItemHolderLike> = HTDefaultColor.entries.associateWith {
        HTConst.MINECRAFT.toId(path.replace("%s", it.serializedName)).toItemLike()
    }
}
