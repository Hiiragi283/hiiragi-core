package hiiragi283.lib.copper

import hiiragi283.lib.registry.toLikeWithItem
import hiiragi283.lib.resource.SimpleBlockItemSupplierWithKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

/**
 * バニラの銅系コンテンツ向けに[HTWeatheringCoppers]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
data object VanillaWeatheringCoppers {
    @JvmField
    val COPPER_BARS: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = Blocks.COPPER_BARS.convert().map(Block::toLikeWithItem)

    @JvmField
    val COPPER_CHAIN: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = Blocks.COPPER_CHAIN.convert().map(Block::toLikeWithItem)

    @JvmField
    val COPPER_LANTERN: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = Blocks.COPPER_LANTERN.convert().map(Block::toLikeWithItem)

    @JvmField
    val COPPER_BLOCK: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.COPPER_BLOCK,
        Blocks.EXPOSED_COPPER,
        Blocks.WEATHERED_COPPER,
        Blocks.OXIDIZED_COPPER,
        Blocks.WAXED_COPPER_BLOCK,
        Blocks.WAXED_EXPOSED_COPPER,
        Blocks.WAXED_WEATHERED_COPPER,
        Blocks.WAXED_OXIDIZED_COPPER,
    )

    @JvmField
    val CUT_COPPER: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.CUT_COPPER,
        Blocks.EXPOSED_CUT_COPPER,
        Blocks.WEATHERED_CUT_COPPER,
        Blocks.OXIDIZED_CUT_COPPER,
        Blocks.WAXED_CUT_COPPER,
        Blocks.WAXED_EXPOSED_CUT_COPPER,
        Blocks.WAXED_WEATHERED_CUT_COPPER,
        Blocks.WAXED_OXIDIZED_CUT_COPPER,
    )

    @JvmField
    val CHISELED_COPPER: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.CHISELED_COPPER,
        Blocks.EXPOSED_CHISELED_COPPER,
        Blocks.WEATHERED_CHISELED_COPPER,
        Blocks.OXIDIZED_CHISELED_COPPER,
        Blocks.WAXED_CHISELED_COPPER,
        Blocks.WAXED_EXPOSED_CHISELED_COPPER,
        Blocks.WAXED_WEATHERED_CHISELED_COPPER,
        Blocks.WAXED_OXIDIZED_CHISELED_COPPER,
    )

    @JvmField
    val CUT_COPPER_STAIRS: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.CUT_COPPER_STAIRS,
        Blocks.EXPOSED_CUT_COPPER_STAIRS,
        Blocks.WEATHERED_CUT_COPPER_STAIRS,
        Blocks.OXIDIZED_CUT_COPPER_STAIRS,
        Blocks.WAXED_CUT_COPPER_STAIRS,
        Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS,
        Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS,
        Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
    )

    @JvmField
    val CUT_COPPER_SLAB: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.CUT_COPPER_SLAB,
        Blocks.EXPOSED_CUT_COPPER_SLAB,
        Blocks.WEATHERED_CUT_COPPER_SLAB,
        Blocks.OXIDIZED_CUT_COPPER_SLAB,
        Blocks.WAXED_CUT_COPPER_SLAB,
        Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB,
        Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB,
        Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB,
    )

    @JvmField
    val COPPER_DOOR: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.COPPER_DOOR,
        Blocks.EXPOSED_COPPER_DOOR,
        Blocks.WEATHERED_COPPER_DOOR,
        Blocks.OXIDIZED_COPPER_DOOR,
        Blocks.WAXED_COPPER_DOOR,
        Blocks.WAXED_EXPOSED_COPPER_DOOR,
        Blocks.WAXED_WEATHERED_COPPER_DOOR,
        Blocks.WAXED_OXIDIZED_COPPER_DOOR,
    )

    @JvmField
    val COPPER_TRAPDOOR: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.COPPER_TRAPDOOR,
        Blocks.EXPOSED_COPPER_TRAPDOOR,
        Blocks.WEATHERED_COPPER_TRAPDOOR,
        Blocks.OXIDIZED_COPPER_TRAPDOOR,
        Blocks.WAXED_COPPER_TRAPDOOR,
        Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR,
        Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR,
        Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR,
    )

    @JvmField
    val COPPER_GRATE: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.COPPER_GRATE,
        Blocks.EXPOSED_COPPER_GRATE,
        Blocks.WEATHERED_COPPER_GRATE,
        Blocks.OXIDIZED_COPPER_GRATE,
        Blocks.WAXED_COPPER_GRATE,
        Blocks.WAXED_EXPOSED_COPPER_GRATE,
        Blocks.WAXED_WEATHERED_COPPER_GRATE,
        Blocks.WAXED_OXIDIZED_COPPER_GRATE,
    )

    @JvmField
    val COPPER_BULB: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.COPPER_BULB,
        Blocks.EXPOSED_COPPER_BULB,
        Blocks.WEATHERED_COPPER_BULB,
        Blocks.OXIDIZED_COPPER_BULB,
        Blocks.WAXED_COPPER_BULB,
        Blocks.WAXED_EXPOSED_COPPER_BULB,
        Blocks.WAXED_WEATHERED_COPPER_BULB,
        Blocks.WAXED_OXIDIZED_COPPER_BULB,
    )

    @JvmField
    val COPPER_CHEST: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.COPPER_CHEST,
        Blocks.EXPOSED_COPPER_CHEST,
        Blocks.WEATHERED_COPPER_CHEST,
        Blocks.OXIDIZED_COPPER_CHEST,
        Blocks.WAXED_COPPER_CHEST,
        Blocks.WAXED_EXPOSED_COPPER_CHEST,
        Blocks.WAXED_WEATHERED_COPPER_CHEST,
        Blocks.WAXED_OXIDIZED_COPPER_CHEST,
    )

    @JvmField
    val COPPER_GOLEM_STATUE: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.COPPER_GOLEM_STATUE,
        Blocks.EXPOSED_COPPER_GOLEM_STATUE,
        Blocks.WEATHERED_COPPER_GOLEM_STATUE,
        Blocks.OXIDIZED_COPPER_GOLEM_STATUE,
        Blocks.WAXED_COPPER_GOLEM_STATUE,
        Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE,
        Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE,
        Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE,
    )

    @JvmField
    val LIGHTNING_ROD: HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = create(
        Blocks.LIGHTNING_ROD,
        Blocks.EXPOSED_LIGHTNING_ROD,
        Blocks.WEATHERED_LIGHTNING_ROD,
        Blocks.OXIDIZED_LIGHTNING_ROD,
        Blocks.WAXED_LIGHTNING_ROD,
        Blocks.WAXED_EXPOSED_LIGHTNING_ROD,
        Blocks.WAXED_WEATHERED_LIGHTNING_ROD,
        Blocks.WAXED_OXIDIZED_LIGHTNING_ROD,
    )

    @JvmStatic
    private fun create(
        unaffected: Block,
        exposed: Block,
        weathered: Block,
        oxidized: Block,
        waxedUnaffected: Block,
        waxedExposed: Block,
        waxedWeathered: Block,
        waxedOxidized: Block,
    ): HTWeatheringCoppers<SimpleBlockItemSupplierWithKey> = HTWeatheringCoppers(
        HTCopperCollection(unaffected, exposed, weathered, oxidized),
        HTCopperCollection(waxedUnaffected, waxedExposed, waxedWeathered, waxedOxidized),
    ).map(Block::toLikeWithItem)
}
