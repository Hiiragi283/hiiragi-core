package hiiragi283.core.api.copper

import hiiragi283.core.api.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.core.api.resource.vanillaId

/**
 * バニラの銅系コンテンツ向けに[HTWeatheringCoppers]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
data object VanillaWeatheringCoppers {
    @JvmField
    val COPPER_BLOCK: HTWeatheringCoppers<HTSimpleDeferredBlockAndItem> = HTWeatheringCoppers(
        { phase: HTCopperPhase ->
            when (phase) {
                HTCopperPhase.UNAFFECTED -> "copper_block"
                else -> phase.createPath("copper")
            }.let(::vanillaId)
                .let(::HTSimpleDeferredBlockAndItem)
        },
        { phase: HTCopperPhase -> HTSimpleDeferredBlockAndItem(vanillaId(phase.createWaxedPath("copper"))) },
    )

    @JvmField
    val CUT_COPPER: HTWeatheringCoppers<HTSimpleDeferredBlockAndItem> = create("cut_copper")

    @JvmField
    val CHISELED_COPPER: HTWeatheringCoppers<HTSimpleDeferredBlockAndItem> = create("chiseled_copper")

    @JvmField
    val CUT_COPPER_STAIRS: HTWeatheringCoppers<HTSimpleDeferredBlockAndItem> = create("cut_copper_stairs")

    @JvmField
    val CUT_COPPER_SLAB: HTWeatheringCoppers<HTSimpleDeferredBlockAndItem> = create("cut_copper_slab")

    @JvmField
    val COPPER_DOOR: HTWeatheringCoppers<HTSimpleDeferredBlockAndItem> = create("copper_door")

    @JvmField
    val COPPER_TRAPDOOR: HTWeatheringCoppers<HTSimpleDeferredBlockAndItem> = create("copper_trapdoor")

    @JvmField
    val COPPER_GRATE: HTWeatheringCoppers<HTSimpleDeferredBlockAndItem> = create("copper_grate")

    @JvmField
    val COPPER_BULB: HTWeatheringCoppers<HTSimpleDeferredBlockAndItem> = create("copper_bulb")

    @JvmStatic
    private fun create(name: String): HTWeatheringCoppers<HTSimpleDeferredBlockAndItem> = HTWeatheringCoppers(
        { phase: HTCopperPhase -> HTSimpleDeferredBlockAndItem(vanillaId(phase.createPath(name))) },
        { phase: HTCopperPhase -> HTSimpleDeferredBlockAndItem(vanillaId(phase.createWaxedPath(name))) },
    )
}
