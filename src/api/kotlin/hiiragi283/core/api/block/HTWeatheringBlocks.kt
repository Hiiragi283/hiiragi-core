package hiiragi283.core.api.block

import hiiragi283.core.api.registry.HTSimpleBlockHolderLike
import hiiragi283.core.api.registry.toLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

/**
 * 既存の銅系ブロックをまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.14.0
 */
object HTWeatheringBlocks {
    @JvmField
    val COPPER_BLOCK: HTWeatheringBlockMap = createMap(
        createMap(Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER),
        createMap(Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_EXPOSED_COPPER, Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_OXIDIZED_COPPER),
    )

    @JvmField
    val CUT_COPPER: HTWeatheringBlockMap = createMap(
        createMap(
            Blocks.CUT_COPPER,
            Blocks.EXPOSED_CUT_COPPER,
            Blocks.WEATHERED_CUT_COPPER,
            Blocks.OXIDIZED_CUT_COPPER,
        ),
        createMap(
            Blocks.WAXED_CUT_COPPER,
            Blocks.WAXED_EXPOSED_CUT_COPPER,
            Blocks.WAXED_WEATHERED_CUT_COPPER,
            Blocks.WAXED_OXIDIZED_CUT_COPPER,
        ),
    )

    @JvmField
    val CHISELED_COPPER: HTWeatheringBlockMap = createMap(
        createMap(
            Blocks.CHISELED_COPPER,
            Blocks.EXPOSED_CHISELED_COPPER,
            Blocks.WEATHERED_CHISELED_COPPER,
            Blocks.OXIDIZED_CHISELED_COPPER,
        ),
        createMap(
            Blocks.WAXED_CHISELED_COPPER,
            Blocks.WAXED_EXPOSED_CHISELED_COPPER,
            Blocks.WAXED_WEATHERED_CHISELED_COPPER,
            Blocks.WAXED_OXIDIZED_CHISELED_COPPER,
        ),
    )

    @JvmStatic
    private fun createMap(
        base: Map<HTWeatheringLevel, HTSimpleBlockHolderLike>,
        waxed: Map<HTWeatheringLevel, HTSimpleBlockHolderLike>,
    ): HTWeatheringBlockMap = HTWeatheringBlockMap(base, waxed)

    @JvmStatic
    private fun createMap(
        unaffected: Block,
        exposed: Block,
        weathered: Block,
        oxidized: Block,
    ): Map<HTWeatheringLevel, HTSimpleBlockHolderLike> = mapOf(
        HTWeatheringLevel.UNAFFECTED to unaffected,
        HTWeatheringLevel.EXPOSED to exposed,
        HTWeatheringLevel.WEATHERED to weathered,
        HTWeatheringLevel.OXIDIZED to oxidized,
    ).mapValues { (_, block: Block) -> block.toLike() }
}
