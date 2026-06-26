package hiiragi283.lib.copper

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

data object VanillaWeatheringCoppers {
    @JvmField
    val COPPER_BARS: HTWeatheringCoppers<Block> = Blocks.COPPER_BARS.convert()

    @JvmField
    val COPPER_CHAIN: HTWeatheringCoppers<Block> = Blocks.COPPER_CHAIN.convert()

    @JvmField
    val COPPER_LANTERN: HTWeatheringCoppers<Block> = Blocks.COPPER_LANTERN.convert()

    @JvmField
    val COPPER_BLOCK: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
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
    val CUT_COPPER: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
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
    val CHISELED_COPPER: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
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
    val CUT_COPPER_STAIRS: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
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
    val CUT_COPPER_SLAB: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
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
    val COPPER_DOOR: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
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
    val COPPER_TRAPDOOR: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
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
    val COPPER_GRATE: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
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
    val COPPER_BULB: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
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
    val COPPER_CHEST: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
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
    val COPPER_GOLEM_STATUE: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
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
    val LIGHTNING_ROD: HTWeatheringCoppers<Block> = HTWeatheringCoppers(
        Blocks.LIGHTNING_ROD,
        Blocks.EXPOSED_LIGHTNING_ROD,
        Blocks.WEATHERED_LIGHTNING_ROD,
        Blocks.OXIDIZED_LIGHTNING_ROD,
        Blocks.WAXED_LIGHTNING_ROD,
        Blocks.WAXED_EXPOSED_LIGHTNING_ROD,
        Blocks.WAXED_WEATHERED_LIGHTNING_ROD,
        Blocks.WAXED_OXIDIZED_LIGHTNING_ROD,
    )
}
