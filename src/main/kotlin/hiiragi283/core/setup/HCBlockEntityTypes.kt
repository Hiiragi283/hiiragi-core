package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTDeferredBlockEntityType
import hiiragi283.core.api.registry.HTDeferredBlockEntityTypeRegister
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.block.entity.HTCopperBasinBlockEntity
import hiiragi283.core.common.block.entity.HTTestBlockEntity
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType

object HCBlockEntityTypes {
    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    private fun <BE : HTBlockEntity> registerTick(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<BE>,
        blockBuilder: MutableSet<Block>.() -> Unit,
    ): HTDeferredBlockEntityType<BE> = REGISTER.registerType(name, factory, HTBlockEntity::tickServer, HTBlockEntity::tickClient, blockBuilder)

    @JvmField
    val COPPER_BASIN: HTDeferredBlockEntityType<HTCopperBasinBlockEntity> = registerTick("copper_basin", ::HTCopperBasinBlockEntity) { HCBlocks.COPPER_BASIN.allCoppers.map { it.get() }.let(::addAll) }

    @JvmField
    val TEST: HTDeferredBlockEntityType<HTTestBlockEntity> = registerTick("test", ::HTTestBlockEntity) { add(HCBlocks.TEST.get()) }
}
