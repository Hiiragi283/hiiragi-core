package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.common.block.HTTestBlock
import hiiragi283.core.common.block.HTTreeTapBlock
import hiiragi283.core.common.block.HTWarpedWartBlock
import hiiragi283.core.common.block.cauldron.HTLatexCauldronBlock
import hiiragi283.core.common.item.block.HTWarpedWartItem
import hiiragi283.core.common.registry.HTBasicDeferredBlockAndItem
import hiiragi283.core.common.registry.HTDeferredBlockAndItem
import hiiragi283.core.common.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.core.common.registry.register.HTDeferredBlockAndItemRegister
import hiiragi283.core.common.registry.register.HTDeferredBlockRegister
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
        copyOf(Blocks.NETHER_WART),
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
    val LATEX_CAULDRON: HTBlockHolderLike<HTLatexCauldronBlock> = REGISTER_ONLY_BLOCK.registerBlock(
        "latex_cauldron",
        copyOf(Blocks.CAULDRON).randomTicks(),
        ::HTLatexCauldronBlock,
    )

    @JvmField
    val EXP_DRAIN: HTSimpleDeferredBlockAndItem = REGISTER.registerSimple(
        "exp_drain",
        properties(5f, 6f).sound(SoundType.METAL).noCollission(),
    )

    @JvmField
    val TEST: HTBasicDeferredBlockAndItem<HTTestBlock> = REGISTER.registerSimple("test", unbreakable(), ::HTTestBlock)

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties =
        BlockBehaviour.Properties.of().strength(hardness, resistance)

    @JvmStatic
    private fun unbreakable(): BlockBehaviour.Properties = properties(-1f, 3600000f)
}
