package hiiragi283.core.api

import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.resource.toId
import net.minecraft.world.item.DyeColor

object VanillaColoredContents {
    @JvmField
    val BANNER: HTColoredContents<HTSimpleItemHolderLike> = create("%s_banner")

    @JvmField
    val BED: HTColoredContents<HTSimpleItemHolderLike> = create("%s_bed")

    @JvmField
    val CANDLE: HTColoredContents<HTSimpleItemHolderLike> = create("%s_candle")

    @JvmField
    val CARPET: HTColoredContents<HTSimpleItemHolderLike> = create("%s_carpet")

    @JvmField
    val CONCRETE: HTColoredContents<HTSimpleItemHolderLike> = create("%s_concrete")

    @JvmField
    val CONCRETE_POWDER: HTColoredContents<HTSimpleItemHolderLike> = create("%s_concrete_powder")

    @JvmField
    val DYE: HTColoredContents<HTSimpleItemHolderLike> = create("%s_dye")

    @JvmField
    val GLAZED_TERRACOTTA: HTColoredContents<HTSimpleItemHolderLike> = create("%s_glazed_terracotta")

    @JvmField
    val SHULKER_BOX: HTColoredContents<HTSimpleItemHolderLike> = create("%s_shulker_box")

    @JvmField
    val STAINED_GLASS: HTColoredContents<HTSimpleItemHolderLike> = create("%s_stained_glass")

    @JvmField
    val STAINED_GLASS_PANE: HTColoredContents<HTSimpleItemHolderLike> = create("%s_stained_glass_pane")

    @JvmField
    val TERRACOTTA: HTColoredContents<HTSimpleItemHolderLike> = create("%s_terracotta")

    @JvmField
    val WOOL: HTColoredContents<HTSimpleItemHolderLike> = create("%s_wool")

    @JvmStatic
    private fun create(path: String): HTColoredContents<HTSimpleItemHolderLike> = object : HTColoredContents<HTSimpleItemHolderLike> {
        val map: Map<HTDefaultColor, HTSimpleItemHolderLike> = HTDefaultColor.entries.associateWith {
            HTConst.MINECRAFT.toId(path.replace("%s", it.serializedName)).toItemLike()
        }

        override fun get(color: HTDefaultColor): HTSimpleItemHolderLike? = map[color]

        override fun get(color: DyeColor): HTSimpleItemHolderLike? = HTDefaultColor.fromDye(color).let(::get)

        override fun iterator(): Iterator<Pair<HTDefaultColor, HTSimpleItemHolderLike>> = map.toList().iterator()
    }
}
