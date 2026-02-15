package hiiragi283.core.common.material

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.registry.HTSimpleDeferredItem

object ColoredMaterials {
    @JvmField
    val BANNER: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_banner")

    @JvmField
    val BED: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_bed")

    @JvmField
    val CANDLE: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_candle")

    @JvmField
    val CARPET: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_carpet")

    @JvmField
    val CONCRETE: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_concrete")

    @JvmField
    val CONCRETE_POWDER: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_concrete_powder")

    @JvmField
    val DYE: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_dye")

    @JvmField
    val GLAZED_TERRACOTTA: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_glazed_terracotta")

    @JvmField
    val SHULKER_BOX: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_shulker_box")

    @JvmField
    val STAINED_GLASS: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_stained_glass")

    @JvmField
    val STAINED_GLASS_PANE: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_stained_glass_pane")

    @JvmField
    val TERRACOTTA: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_terracotta")

    @JvmField
    val WOOL: Map<HTDefaultColor, HTSimpleDeferredItem> = create("%s_wool")

    @JvmStatic
    private fun create(path: String): Map<HTDefaultColor, HTSimpleDeferredItem> = HTDefaultColor.entries.associateWith {
        HTSimpleDeferredItem(HTConst.MINECRAFT.toId(path.replace("%s", it.serializedName)))
    }
}
