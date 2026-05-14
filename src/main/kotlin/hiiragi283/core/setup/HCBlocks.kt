package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.block.HTCopperBasinBlock
import hiiragi283.core.common.block.HTWeatheringCopperBasinBlock
import hiiragi283.lib.item.HTBlockItem
import hiiragi283.lib.registry.HTDeferredBlockAndItemRegister
import hiiragi283.lib.registry.HTDeferredBlockRegister
import hiiragi283.lib.registry.HTWeatheringCopperBlocks
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
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

    //    Misc    //

    @JvmField
    val COPPER_BASIN: HTWeatheringCopperBlocks<HTCopperBasinBlock, HTWeatheringCopperBasinBlock, HTBlockItem<Block>> = HTWeatheringCopperBlocks.createSimple(
        REGISTER,
        "copper_basin",
        {
            BlockBehaviour.Properties.of()
                .requiresCorrectToolForDrops()
                .strength(2f)
                .noOcclusion()
                .sound(SoundType.COPPER)
        },
        ::HTCopperBasinBlock,
        ::HTWeatheringCopperBasinBlock,
    )
}
