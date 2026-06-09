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
    @JvmStatic
    val BANNER: ColoredItemContents = ColoredItemContents("%s_banner")

    @JvmStatic
    val BED: ColoredItemContents = ColoredItemContents("%s_bed")

    @JvmStatic
    val CANDLE: ColoredItemContents = ColoredItemContents("%s_candle")

    @JvmStatic
    val CARPET: ColoredItemContents = ColoredItemContents("%s_carpet")

    @JvmStatic
    val CONCRETE: ColoredItemContents = ColoredItemContents("%s_concrete")

    @JvmStatic
    val CONCRETE_POWDER: ColoredItemContents = ColoredItemContents("%s_concrete_powder")

    @JvmStatic
    val DYE: ColoredItemContents = ColoredItemContents("%s_dye")

    @JvmStatic
    val GLAZED_TERRACOTTA: ColoredItemContents = ColoredItemContents("%s_glazed_terracotta")

    @JvmStatic
    val SHULKER_BOX: ColoredItemContents = ColoredItemContents("%s_shulker_box")

    @JvmStatic
    val STAINED_GLASS: ColoredItemContents = ColoredItemContents("%s_stained_glass")

    @JvmStatic
    val STAINED_GLASS_PANE: ColoredItemContents = ColoredItemContents("%s_stained_glass_pane")

    @JvmStatic
    val TERRACOTTA: ColoredItemContents = ColoredItemContents("%s_terracotta")

    @JvmStatic
    val WOOL: ColoredItemContents = ColoredItemContents("%s_wool")

    @JvmInline
    value class ColoredItemContents(private val map: Map<HTDefaultColor, HTSimpleDeferredItem>) : HTColoredContents<HTSimpleDeferredItem> {
        constructor(path: String) : this(HTDefaultColor.entries.associateWithTo(mutableEnumMapOf()) { HTDeferredItem(vanillaId(path.replace("%s", it.serializedName))) })

        override fun get(color: HTDefaultColor): HTSimpleDeferredItem? = map[color]

        override fun get(color: DyeColor): HTSimpleDeferredItem? = HTDefaultColor.fromDye(color).let(::get)

        override fun iterator(): Iterator<Pair<HTDefaultColor, HTSimpleDeferredItem>> = map.toList().iterator()
    }
}
