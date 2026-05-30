package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.block.HTChoppingBoardBlock
import hiiragi283.core.common.block.HTCopperBasinBlock
import hiiragi283.core.common.block.HTWarpedWartBlock
import hiiragi283.core.common.block.HTWeatheringCopperBasinBlock
import hiiragi283.lib.item.HTBlockItem
import hiiragi283.lib.item.component.consumables
import hiiragi283.lib.registry.HTBasicDeferredBlockAndItem
import hiiragi283.lib.registry.HTDeferredBlockAndItemRegister
import hiiragi283.lib.registry.HTDeferredBlockRegister
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTWeatheringCopperBlocks
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus

data object HCBlocks {
    @JvmField
    val REGISTER_ONLY_BLOCK = HTDeferredBlockRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val REGISTER = HTDeferredBlockAndItemRegister(REGISTER_ONLY_BLOCK)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    //    Resources    //

    @JvmField
    val CHARCOAL_BLOCK: HTSimpleDeferredBlockAndItem = REGISTER.registerSimple("charcoal_block", copyOf(Blocks.COAL_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN))

    @JvmField
    val ECHO_BLOCK: HTSimpleDeferredBlockAndItem = REGISTER.registerSimple("echo_block", copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_CYAN))

    //    Crops    //

    @JvmField
    val WARPED_WART: HTBasicDeferredBlockAndItem<HTWarpedWartBlock> = REGISTER.registerSimple(
        "warped_wart",
        copyOf(Blocks.NETHER_WART).mapColor(MapColor.WARPED_WART_BLOCK),
        ::HTWarpedWartBlock,
    ) { prop: Item.Properties -> prop.consumables(HCConsumables.WARPED_WART) }

    //    Misc    //

    @JvmField
    val CHOPPING_BOARD: HTBasicDeferredBlockAndItem<HTChoppingBoardBlock> = REGISTER.registerSimple("chopping_board", copyOf(Blocks.OAK_WOOD), ::HTChoppingBoardBlock)

    @JvmField
    val COPPER_BASIN: HTWeatheringCopperBlocks<HTCopperBasinBlock, HTWeatheringCopperBasinBlock, HTBlockItem<Block>> = HTWeatheringCopperBlocks.createSimple(
        REGISTER,
        "copper_basin",
        {
            properties(2f)
                .requiresCorrectToolForDrops()
                .noOcclusion()
                .sound(SoundType.COPPER)
        },
        ::HTCopperBasinBlock,
        ::HTWeatheringCopperBasinBlock,
    )

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties = BlockBehaviour.Properties.of().strength(hardness, resistance)
}
