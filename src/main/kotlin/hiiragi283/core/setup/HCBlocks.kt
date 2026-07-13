package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.copper.HTCopperPhase
import hiiragi283.core.api.copper.HTWeatheringCoppers
import hiiragi283.core.api.registry.HTBasicDeferredBlockAndItem
import hiiragi283.core.api.registry.HTDeferredBlock
import hiiragi283.core.api.registry.HTDeferredBlockAndItem
import hiiragi283.core.api.registry.HTDeferredBlockAndItemRegister
import hiiragi283.core.api.registry.HTDeferredBlockRegister
import hiiragi283.core.api.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.core.common.block.HTCopperBasinBlock
import hiiragi283.core.common.block.HTTestBlock
import hiiragi283.core.common.block.HTTreeTapBlock
import hiiragi283.core.common.block.HTWarpedWartBlock
import hiiragi283.core.common.block.HTWeatheringCopperBasinBlock
import hiiragi283.core.common.item.block.HTWarpedWartItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.neoforged.bus.api.IEventBus

object HCBlocks {
    @JvmField
    val REGISTER_ONLY_BLOCK = HTDeferredBlockRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val REGISTER = HTDeferredBlockAndItemRegister(REGISTER_ONLY_BLOCK)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    //    Materials    //

    @JvmField
    val OIL_SAND: HTSimpleDeferredBlockAndItem = REGISTER.registerSimple(
        "oil_sand",
        copyOf(Blocks.SAND).mapColor(MapColor.COLOR_BLACK),
    )

    @JvmField
    val OIL_SHALE: HTSimpleDeferredBlockAndItem = REGISTER.registerSimple(
        "oil_shale",
        copyOf(Blocks.STONE).mapColor(MapColor.COLOR_BLACK),
    )

    //    Crops    //

    @JvmField
    val WARPED_WART: HTDeferredBlockAndItem<HTWarpedWartBlock, HTWarpedWartItem> = REGISTER.register(
        "warped_wart",
        copyOf(Blocks.NETHER_WART).mapColor(MapColor.WARPED_WART_BLOCK),
        ::HTWarpedWartBlock,
        ::HTWarpedWartItem,
    )

    //    Misc    //

    // Basic
    @JvmField
    val TREE_TAP: HTBasicDeferredBlockAndItem<HTTreeTapBlock> = REGISTER.registerSimple(
        "tree_tap",
        properties(3.5f, 16f)
            .sound(SoundType.LANTERN)
            .pushReaction(PushReaction.DESTROY)
            .noOcclusion()
            .randomTicks(),
        ::HTTreeTapBlock,
    )

    @JvmField
    val COPPER_BASIN: HTWeatheringCoppers<HTBasicDeferredBlockAndItem<HTCopperBasinBlock>> = run {
        val name = "copper_basin"
        val prop: BlockBehaviour.Properties = properties(2f)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .sound(SoundType.COPPER)
        HTWeatheringCoppers(
            { phase: HTCopperPhase -> REGISTER.registerSimple(phase.createPath(name), prop, blockFactory = { HTWeatheringCopperBasinBlock(phase.toState(), it) }) },
            { phase: HTCopperPhase -> REGISTER.registerSimple(phase.createWaxedPath(name), prop, ::HTCopperBasinBlock) },
        )
    }

    @JvmField
    val TEST: HTDeferredBlock<HTTestBlock> = REGISTER_ONLY_BLOCK.registerBlock(
        "test",
        unbreakable().requiredFeatures(HiiragiCoreAPI.EXPERIMENTAL).noLootTable(),
        ::HTTestBlock,
    )

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties = BlockBehaviour.Properties.of().strength(hardness, resistance)

    @JvmStatic
    private fun unbreakable(): BlockBehaviour.Properties = properties(-1f, 3600000f)
}
