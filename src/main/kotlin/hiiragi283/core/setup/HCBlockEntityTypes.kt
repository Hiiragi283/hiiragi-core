package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.block.entity.HTCopperBasinBlockEntity
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.registry.HTDeferredBlockEntityTypeRegister
import hiiragi283.lib.transfer.HTHandlerProvider
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent

data object HCBlockEntityTypes {
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

    //    Event    //

    // Supported Blocks
    @JvmStatic
    private fun addSupportedBlocks(event: BlockEntityTypeAddBlocksEvent) {
        /*for (holder: HTBlockHolderLike<*> in HCBlocks.REGISTER.asBlockSequence()) {
            val block: Block = holder.get()
            if (block is HTBlockWithEntity) {
                event.modify(block.getBlockEntityType().get(), block)
            }
        }*/
        HiiragiCoreAPI.LOGGER.info("Added supported blocks to BlockEntityType!")
    }

    // Capabilities
    @JvmStatic
    private fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        registerHandler(event, COPPER_BASIN.get())

        HiiragiCoreAPI.LOGGER.info("Registered Block Capabilities!")
    }

    @JvmStatic
    private fun <BE> registerHandler(
        event: RegisterCapabilitiesEvent,
        type: BlockEntityType<BE>,
    ) where BE : BlockEntity, BE : HTHandlerProvider {
        event.registerBlockEntity(Capabilities.Item.BLOCK, type, HTHandlerProvider::getItemHandler)
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, type, HTHandlerProvider::getFluidHandler)
        event.registerBlockEntity(Capabilities.Energy.BLOCK, type, HTHandlerProvider::getEnergyStorage)
    }
}
