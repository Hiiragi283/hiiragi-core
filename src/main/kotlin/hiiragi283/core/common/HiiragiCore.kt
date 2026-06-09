package hiiragi283.core.common

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.item.HTPotionBucketItem
import hiiragi283.core.common.material.HCMaterials
import hiiragi283.core.setup.HCAttachmentTypes
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCCreativeTabs
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCMiscRegister
import hiiragi283.core.setup.HCRecipeLookups
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.item.alchemy.HTPotionFluidManager
import hiiragi283.lib.material.HTModifyMaterialContentsEvent
import hiiragi283.lib.mod.HTCommonMod
import hiiragi283.lib.network.HTPayloadHandlers
import hiiragi283.lib.network.HTUpdateBlockEntityPacket
import hiiragi283.lib.network.HTUpdateMenuPacket
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.transfer.access.ItemAccess
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCore : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        HiiragiCoreAPI.LOGGER.info("Initializing Hiiragi-Core...")

        eventBus.addListener(HCMiscRegister::register)

        NeoForgeMod.enableMergedAttributeTooltips()
        NeoForgeMod.enableMilkFluid()

        HCDataComponents.REGISTER.register(eventBus)

        HCFluids.register(eventBus)
        HCBlocks.register(eventBus)
        HCItems.register(eventBus)

        HCAttachmentTypes.REGISTER.register(eventBus)
        HCBlockEntityTypes.REGISTER.register(eventBus)
        HCCreativeTabs.REGISTER.register(eventBus)
        HCRecipeSerializers.REGISTER.register(eventBus)
        HCRecipeTypes.REGISTER.register(eventBus)

        HiiragiCoreAPI.LOGGER.info("Hiiragi-Core initialized")
    }

    override fun onConstruct(event: FMLConstructModEvent) {
        event.enqueueWork(HCMaterials::initTags)
    }

    override fun registerRegistries(event: NewRegistryEvent) {
        event.register(HTRegistries.ITEM_RESULT_SERIALIZER)
        event.register(HTRegistries.MATERIAL_CONTENTS)
        event.register(HTRegistries.SLOT_TYPE)
    }

    override fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork(HCRecipeLookups::init)
        event.enqueueWork {
            HTRegistries.MATERIAL_CONTENTS
                .listElements()
                .map(::HTModifyMaterialContentsEvent)
                .forEach(MOD_BUS::post)
        }
        event.enqueueWork { HTPotionFluidManager.register(HCFluids.POTION.get(), HTPlatformImpl.DEFAULT_POTION_HANDLER) }
    }

    override fun registerCapabilities(helper: CapabilityHelper) {
        // Block
        helper.registerBlockEntity(HCBlockEntityTypes.CHOPPING_BOARD.get())
        helper.registerBlockEntity(HCBlockEntityTypes.COPPER_BASIN.get())
        helper.registerBlockEntity(HCBlockEntityTypes.FORGING_ANVIL.get())
        // Item
        helper.registerItem(
            Capabilities.Fluid.ITEM,
            { _, access: ItemAccess -> HTPotionBucketItem.BucketHandler(access) },
            HCFluids.POTION.bucketHolder,
        )
    }

    override fun registerPayload(registrar: PayloadRegistrar) {
        registrar.playToClient(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC, HTPayloadHandlers::handleS2C)
        registrar.playBidirectional(HTUpdateMenuPacket.TYPE, HTUpdateMenuPacket.STREAM_CODEC, HTPayloadHandlers::handleS2C, HTPayloadHandlers::handleC2S)
    }
}
