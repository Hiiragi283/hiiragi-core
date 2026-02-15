package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.block.HTTestBlock
import hiiragi283.core.common.block.HTWarpedWartBlock
import hiiragi283.core.common.item.block.HTWarpedWartItem
import hiiragi283.core.common.registry.HTBasicDeferredBlock
import hiiragi283.core.common.registry.HTDeferredBlock
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.common.registry.register.HTDeferredBlockRegister
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus

object HCBlocks {
    @JvmField
    val REGISTER = HTDeferredBlockRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    //    Materials    //

    @JvmField
    val OIL_SAND: HTSimpleDeferredBlock = REGISTER.registerSimple(
        "oil_sand",
        copyOf(Blocks.SAND).mapColor(MapColor.COLOR_BLACK),
    )

    @JvmField
    val OIL_SHALE: HTSimpleDeferredBlock = REGISTER.registerSimple(
        "oil_shale",
        copyOf(Blocks.STONE).mapColor(MapColor.COLOR_BLACK),
    )

    //    Crops    //

    @JvmField
    val WARPED_WART: HTDeferredBlock<HTWarpedWartBlock, HTWarpedWartItem> = REGISTER.register(
        "warped_wart",
        copyOf(Blocks.NETHER_WART),
        ::HTWarpedWartBlock,
        ::HTWarpedWartItem,
    )

    //    Misc    //

    @JvmField
    val EXP_DRAIN: HTSimpleDeferredBlock = REGISTER.registerSimple("exp_drain", properties(5f, 6f).sound(SoundType.METAL).noCollission())

    @JvmField
    val TEST: HTBasicDeferredBlock<HTTestBlock> = REGISTER.registerSimple("test", unbreakable(), ::HTTestBlock)

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties =
        BlockBehaviour.Properties.of().strength(hardness, resistance)

    @JvmStatic
    private fun unbreakable(): BlockBehaviour.Properties = properties(-1f, 3600000f)
}
