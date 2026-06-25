package hiiragi283.lib.color

import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId

/**
 * バニラの色付きアイテム向けに[HTColoredCollection]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object VanillaColoredCollections {
    @JvmField
    val BANNER: HTColoredCollection<HTSimpleDeferredItem> = create("%s_banner")

    @JvmField
    val BED: HTColoredCollection<HTSimpleDeferredItem> = create("%s_bed")

    @JvmField
    val CANDLE: HTColoredCollection<HTSimpleDeferredItem> = create("%s_candle")

    @JvmField
    val CARPET: HTColoredCollection<HTSimpleDeferredItem> = create("%s_carpet")

    @JvmField
    val CONCRETE: HTColoredCollection<HTSimpleDeferredItem> = create("%s_concrete")

    @JvmField
    val CONCRETE_POWDER: HTColoredCollection<HTSimpleDeferredItem> = create("%s_concrete_powder")

    @JvmField
    val DYE: HTColoredCollection<HTSimpleDeferredItem> = create("%s_dye")

    @JvmField
    val GLAZED_TERRACOTTA: HTColoredCollection<HTSimpleDeferredItem> = create("%s_glazed_terracotta")

    @JvmField
    val SHULKER_BOX: HTColoredCollection<HTSimpleDeferredItem> = create("%s_shulker_box")

    @JvmField
    val STAINED_GLASS: HTColoredCollection<HTSimpleDeferredItem> = create("%s_stained_glass")

    @JvmField
    val STAINED_GLASS_PANE: HTColoredCollection<HTSimpleDeferredItem> = create("%s_stained_glass_pane")

    @JvmField
    val TERRACOTTA: HTColoredCollection<HTSimpleDeferredItem> = create("%s_terracotta")

    @JvmField
    val WOOL: HTColoredCollection<HTSimpleDeferredItem> = create("%s_wool")

    @JvmStatic
    private fun create(path: String): HTColoredCollection<HTSimpleDeferredItem> = HTColoredCollection { HTSimpleDeferredItem(vanillaId(path.replace("%s", it.serializedName))) }
}
