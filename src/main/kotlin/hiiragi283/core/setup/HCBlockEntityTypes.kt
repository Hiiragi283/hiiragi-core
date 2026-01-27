package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.common.block.HTBlockWithEntity
import hiiragi283.core.common.capability.HTEnergyCapabilities
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.capability.HTItemCapabilities
import hiiragi283.core.common.registry.HTDeferredOnlyBlock
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

    //    Event    //

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlocks(event: BlockEntityTypeAddBlocksEvent) {
        for (holder: HTDeferredOnlyBlock<*> in HCBlocks.REGISTER.asBlockSequence()) {
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
