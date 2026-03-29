package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.block.HTWarpedWartBlock
import hiiragi283.core.impl.registry.HTBasicDeferredBlockAndItem
import hiiragi283.core.impl.registry.HTDeferredBlockAndItemRegister
import hiiragi283.core.impl.registry.HTDeferredBlockRegister
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
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

    @JvmField
    val WARPED_WART: HTBasicDeferredBlockAndItem<HTWarpedWartBlock> = REGISTER.registerSimple(
        "warped_wart",
        copyOf(Blocks.NETHER_WART).mapColor(MapColor.WARPED_WART_BLOCK),
        ::HTWarpedWartBlock,
    ) { prop: Item.Properties -> prop.food(HCFoods.WARPED_WART, HCConsumables.WARPED_WART) }

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties =
        BlockBehaviour.Properties.of().strength(hardness, resistance)

    @JvmStatic
    private fun unbreakable(): BlockBehaviour.Properties = properties(-1f, 3600000f)
}
