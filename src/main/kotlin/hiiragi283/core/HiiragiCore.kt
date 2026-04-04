package hiiragi283.core

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import hiiragi283.core.api.network.HTPayloadHandlers
import hiiragi283.core.common.material.VanillaMaterials
import hiiragi283.core.common.network.HTUpdateBlockEntityPacket
import hiiragi283.core.common.network.HTUpdateMenuPacket
import hiiragi283.core.impl.HiiragiCoreAccessImpl
import hiiragi283.core.setup.HCAttachmentTypes
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCCreativeTabs
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCMaterialComponents
import hiiragi283.core.setup.HCMaterials
import hiiragi283.core.setup.HCMenuTypes
import hiiragi283.core.setup.HCMiscRegister
import hiiragi283.core.setup.HCRecipeBookCategories
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.core.setup.HCWidgetTypes
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.NewRegistryEvent

@Mod(HiiragiCoreAPI.MOD_ID)
class HiiragiCore(eventBus: IEventBus, container: ModContainer) {
    init {
        HiiragiCoreAPI.LOGGER.info("Hiiragi Core is loading...")

        eventBus.addListener(::registerRegistries)
        eventBus.addListener(HCMiscRegister::register)
        eventBus.addListener(::commonSetup)

        eventBus.addListener { event: RegisterPayloadHandlersEvent ->
            val registrar: PayloadRegistrar = container.modInfo
                .version
                .toString()
                .let(event::registrar)
            registrar.playToClient(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC, HTPayloadHandlers::handleS2C)
            registrar.playBidirectional(
                HTUpdateMenuPacket.TYPE,
                HTUpdateMenuPacket.STREAM_CODEC,
                HTPayloadHandlers::handleS2C,
                HTPayloadHandlers::handleC2S,
            )
        }

        HCDataComponents.REGISTER.register(eventBus)
        HCMaterialComponents.REGISTER.register(eventBus)

        HCFluids.register(eventBus)
        HCBlocks.register(eventBus)
        HCItems.register(eventBus)

        VanillaMaterials.REGISTER.register(eventBus)
        HCMaterials.REGISTER.register(eventBus)

        HCAttachmentTypes.REGISTER.register(eventBus)
        HCBlockEntityTypes.register(eventBus)
        HCCreativeTabs.REGISTER.register(eventBus)
        HCMenuTypes.REGISTER.register(eventBus)
        HCRecipeBookCategories.REGISTER.register(eventBus)
        HCRecipeSerializers.REGISTER.register(eventBus)
        HCRecipeTypes.REGISTER.register(eventBus)
        HCWidgetTypes.REGISTER.register(eventBus)

        HiiragiCoreAPI.LOGGER.info("Hiiragi Core has been loaded successfully!")
    }

    private fun registerRegistries(event: NewRegistryEvent) {
        event.register(HCRegistries.MATERIAL)
        event.register(HCRegistries.MATERIAL_COMPONENT_TYPE)
        event.register(HCRegistries.SLOT_TYPE)
        event.register(HCRegistries.WIDGET_TYPE)
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork { HTPotionFluidManager.register(HCFluids.POTION.get(), HiiragiCoreAccessImpl.DEFAULT_POTION_HANDLER) }
    }
}
