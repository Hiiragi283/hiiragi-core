package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.block.entity.HTCopperBasinBlockEntity
import hiiragi283.core.common.block.entity.HTTestBlockEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.registry.register.HTDeferredBlockEntityTypeRegister

object HCBlockEntityTypes {
    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val COPPER_BASIN: HTDeferredBlockEntityType<HTCopperBasinBlockEntity> = REGISTER.registerTick("copper_basin", ::HTCopperBasinBlockEntity) { HCBlocks.COPPER_BASIN.allCoppers.map { it.get() }.let(::addAll) }

    @JvmField
    val TEST: HTDeferredBlockEntityType<HTTestBlockEntity> = REGISTER.registerTick("test", ::HTTestBlockEntity) { add(HCBlocks.TEST.get()) }
}
