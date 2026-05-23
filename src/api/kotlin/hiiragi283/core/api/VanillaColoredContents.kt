package hiiragi283.core.api

import hiiragi283.core.api.registry.HTDeferredItem
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.resource.toId
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.ItemLike

/**
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
data object VanillaColoredContents {
    @JvmField
    val BANNER: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_banner")

    @JvmField
    val BED: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_bed")

    @JvmField
    val CANDLE: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_candle")

    @JvmField
    val CARPET: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_carpet")

    @JvmField
    val CONCRETE: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_concrete")

    @JvmField
    val CONCRETE_POWDER: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_concrete_powder")

    @JvmField
    val DYE: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_dye")

    @JvmField
    val GLAZED_TERRACOTTA: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_glazed_terracotta")

    @JvmField
    val SHULKER_BOX: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_shulker_box")

    @JvmField
    val STAINED_GLASS: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_stained_glass")

    @JvmField
    val STAINED_GLASS_PANE: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_stained_glass_pane")

    @JvmField
    val TERRACOTTA: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_terracotta")

    @JvmField
    val WOOL: HTColoredContents<SupplierWithId<ItemLike>> = create("%s_wool")

    @JvmStatic
    private fun create(path: String): HTColoredContents<SupplierWithId<ItemLike>> = object : HTColoredContents<SupplierWithId<ItemLike>> {
        val map: Map<HTDefaultColor, SupplierWithId<ItemLike>> = HTDefaultColor.entries.associateWith {
            HTDeferredItem(HTConst.MINECRAFT.toId(path.replace("%s", it.serializedName)))
        }

        override fun get(color: HTDefaultColor): SupplierWithId<ItemLike>? = map[color]

        override fun get(color: DyeColor): SupplierWithId<ItemLike>? = HTDefaultColor.fromDye(color).let(::get)

        override fun iterator(): Iterator<Pair<HTDefaultColor, SupplierWithId<ItemLike>>> = map.toList().iterator()
    }
}
