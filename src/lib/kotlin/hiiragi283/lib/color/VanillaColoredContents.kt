package hiiragi283.lib.color

import hiiragi283.lib.collection.mutableEnumMapOf
import hiiragi283.lib.registry.HTDeferredItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import net.minecraft.world.item.DyeColor

/**
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
data object VanillaColoredContents {
    @JvmField
    val BANNER: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_banner")

    @JvmField
    val BED: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_bed")

    @JvmField
    val CANDLE: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_candle")

    @JvmField
    val CARPET: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_carpet")

    @JvmField
    val CONCRETE: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_concrete")

    @JvmField
    val CONCRETE_POWDER: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_concrete_powder")

    @JvmField
    val DYE: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_dye")

    @JvmField
    val GLAZED_TERRACOTTA: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_glazed_terracotta")

    @JvmField
    val SHULKER_BOX: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_shulker_box")

    @JvmField
    val STAINED_GLASS: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_stained_glass")

    @JvmField
    val STAINED_GLASS_PANE: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_stained_glass_pane")

    @JvmField
    val TERRACOTTA: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_terracotta")

    @JvmField
    val WOOL: HTColoredContents<HTSimpleDeferredItem> = ColoredItemContents("%s_wool")

    @JvmInline
    private value class ColoredItemContents(private val map: Map<HTDefaultColor, HTSimpleDeferredItem>) : HTColoredContents<HTSimpleDeferredItem> {
        constructor(path: String) : this(HTDefaultColor.entries.associateWithTo(mutableEnumMapOf()) { HTDeferredItem(vanillaId(path.replace("%s", it.serializedName))) })

        override fun get(color: HTDefaultColor): HTSimpleDeferredItem? = map[color]

        override fun get(color: DyeColor): HTSimpleDeferredItem? = HTDefaultColor.fromDye(color).let(::get)

        override fun iterator(): Iterator<Pair<HTDefaultColor, HTSimpleDeferredItem>> = map.toList().iterator()
    }
}
