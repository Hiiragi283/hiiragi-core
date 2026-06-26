package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.block.entity.HTChoppingBoardBlockEntity
import hiiragi283.core.common.block.entity.HTCopperBasinBlockEntity
import hiiragi283.core.common.block.entity.HTForgingAnvilBlockEntity
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.registry.HTDeferredBlockEntityTypeRegister

data object HCBlockEntityTypes {
    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val CHOPPING_BOARD: HTDeferredBlockEntityType<HTChoppingBoardBlockEntity> = REGISTER.registerTick("chopping_board", ::HTChoppingBoardBlockEntity) { add(HCBlocks.CHOPPING_BOARD.get()) }

    @JvmField
    val COPPER_BASIN: HTDeferredBlockEntityType<HTCopperBasinBlockEntity> = REGISTER.registerTick("copper_basin", ::HTCopperBasinBlockEntity) { HCBlocks.COPPER_BASIN.allCoppers.map { it.get() }.let(::addAll) }

    @JvmField
    val FORGING_ANVIL: HTDeferredBlockEntityType<HTForgingAnvilBlockEntity> = REGISTER.registerTick("forging_anvil", ::HTForgingAnvilBlockEntity) { add(HCBlocks.FORGING_ANVIL.get()) }
}
