package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.common.block.HTBlockWithEntity
import hiiragi283.core.common.block.entity.HTCopperBasinBlockEntity
import hiiragi283.core.common.block.entity.HTForgingAnvilBlockEntity
import hiiragi283.core.common.block.entity.HTTestBlockEntity
import hiiragi283.core.common.capability.HTEnergyCapabilities
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.capability.HTItemCapabilities
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.registry.register.HTDeferredBlockEntityTypeRegister
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent

object HCBlockEntityTypes {
    @JvmField
    val REGISTER = HTDeferredBlockEntityTypeRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        eventBus.addListener(::addSupportedBlocks)
        eventBus.addListener(::registerBlockCapabilities)

        REGISTER.register(eventBus)
    }

    @JvmField
    val COPPER_BASIN: HTDeferredBlockEntityType<HTCopperBasinBlockEntity> =
        REGISTER.registerTick("copper_basin", ::HTCopperBasinBlockEntity)

    @JvmField
    val FORGING_ANVIL: HTDeferredBlockEntityType<HTForgingAnvilBlockEntity> =
        REGISTER.registerTick("forging_anvil", ::HTForgingAnvilBlockEntity)

    @JvmField
    val TEST: HTDeferredBlockEntityType<HTTestBlockEntity> = REGISTER.registerTick("test", ::HTTestBlockEntity)

    //    Event    //

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlocks(event: BlockEntityTypeAddBlocksEvent) {
        for (holder: HTBlockHolderLike<*> in HCBlocks.REGISTER.asBlockSequence()) {
            val block: Block = holder.get()
            if (block is HTBlockWithEntity) {
                event.modify(block.getBlockEntityType().get(), block)
            }
        }
        HiiragiCoreAPI.LOGGER.info("Added supported blocks to BlockEntityType!")
    }

    // Capabilities
    @JvmStatic
    private fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        registerHandler(event, COPPER_BASIN.get())
        registerHandler(event, FORGING_ANVIL.get())

        registerHandler(event, TEST.get())

        HiiragiCoreAPI.LOGGER.info("Registered Block Capabilities!")
    }

    @JvmStatic
    private fun <BE> registerHandler(
        event: RegisterCapabilitiesEvent,
        type: BlockEntityType<BE>,
    ) where BE : BlockEntity, BE : HTHandlerProvider {
        event.registerBlockEntity(HTItemCapabilities.block, type, HTHandlerProvider::getItemHandler)
        event.registerBlockEntity(HTFluidCapabilities.block, type, HTHandlerProvider::getFluidHandler)
        event.registerBlockEntity(HTEnergyCapabilities.block, type, HTHandlerProvider::getEnergyStorage)
    }
}
