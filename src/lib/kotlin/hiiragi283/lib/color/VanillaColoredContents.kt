package hiiragi283.lib.color

import hiiragi283.lib.HTConstants
import hiiragi283.lib.registry.HTDeferredItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.toId
import net.minecraft.world.item.DyeColor

/**
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
data object VanillaColoredContents {
    @JvmField
    val BANNER: HTColoredContents<HTSimpleDeferredItem> = create("%s_banner")

    @JvmField
    val BED: HTColoredContents<HTSimpleDeferredItem> = create("%s_bed")

    @JvmField
    val CANDLE: HTColoredContents<HTSimpleDeferredItem> = create("%s_candle")

    @JvmField
    val CARPET: HTColoredContents<HTSimpleDeferredItem> = create("%s_carpet")

    @JvmField
    val CONCRETE: HTColoredContents<HTSimpleDeferredItem> = create("%s_concrete")

    @JvmField
    val CONCRETE_POWDER: HTColoredContents<HTSimpleDeferredItem> = create("%s_concrete_powder")

    @JvmField
    val DYE: HTColoredContents<HTSimpleDeferredItem> = create("%s_dye")

    @JvmField
    val GLAZED_TERRACOTTA: HTColoredContents<HTSimpleDeferredItem> = create("%s_glazed_terracotta")

    @JvmField
    val SHULKER_BOX: HTColoredContents<HTSimpleDeferredItem> = create("%s_shulker_box")

    @JvmField
    val STAINED_GLASS: HTColoredContents<HTSimpleDeferredItem> = create("%s_stained_glass")

    @JvmField
    val STAINED_GLASS_PANE: HTColoredContents<HTSimpleDeferredItem> = create("%s_stained_glass_pane")

    @JvmField
    val TERRACOTTA: HTColoredContents<HTSimpleDeferredItem> = create("%s_terracotta")

    @JvmField
    val WOOL: HTColoredContents<HTSimpleDeferredItem> = create("%s_wool")

    @JvmStatic
    private fun create(path: String): HTColoredContents<HTSimpleDeferredItem> = object : HTColoredContents<HTSimpleDeferredItem> {
        val map: Map<HTDefaultColor, HTSimpleDeferredItem> = HTDefaultColor.entries.associateWith {
            HTDeferredItem(HTConstants.MINECRAFT.toId(path.replace("%s", it.serializedName)))
        }

        override fun get(color: HTDefaultColor): HTSimpleDeferredItem? = map[color]

        override fun get(color: DyeColor): HTSimpleDeferredItem? = HTDefaultColor.fromDye(color).let(::get)

        override fun iterator(): Iterator<Pair<HTDefaultColor, HTSimpleDeferredItem>> = map.toList().iterator()
    }
}
