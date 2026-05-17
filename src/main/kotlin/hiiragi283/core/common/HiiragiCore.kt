package hiiragi283.core.common

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCAttachmentTypes
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCCreativeTabs
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCMaterialContents
import hiiragi283.core.setup.HCMiscRegister
import hiiragi283.core.setup.HCRecipeLookups
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.material.HTModifyMaterialContentsEvent
import hiiragi283.lib.mod.HTCommonMod
import hiiragi283.lib.network.HTPayloadHandlers
import hiiragi283.lib.network.HTUpdateBlockEntityPacket
import hiiragi283.lib.network.HTUpdateMenuPacket
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.NewRegistryEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCore : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        HiiragiCoreAPI.LOGGER.info("Initializing Hiiragi-Core...")

        eventBus.addListener(HCMiscRegister::register)

        NeoForgeMod.enableMergedAttributeTooltips()
        NeoForgeMod.enableMilkFluid()

        HCBlocks.register(eventBus)
        HCItems.register(eventBus)

        HCAttachmentTypes.REGISTER.register(eventBus)
        HCBlockEntityTypes.register(eventBus)
        HCCreativeTabs.REGISTER.register(eventBus)
        HCMaterialContents.REGISTER.register(eventBus)
        HCRecipeSerializers.REGISTER.register(eventBus)
        HCRecipeTypes.REGISTER.register(eventBus)

        HiiragiCoreAPI.LOGGER.info("Hiiragi-Core initialized")
    }

    override fun registerRegistries(event: NewRegistryEvent) {
        event.register(HTRegistries.ITEM_RESULT_SERIALIZER)
        event.register(HTRegistries.MATERIAL_CONTENTS)
        event.register(HTRegistries.SLOT_TYPE)
        event.register(HTRegistries.WIDGET_TYPE)
    }

    override fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork(HCRecipeLookups::init)
        event.enqueueWork {
            HTRegistries.MATERIAL_CONTENTS
                .listElements()
                .map(::HTModifyMaterialContentsEvent)
                .forEach(MOD_BUS::post)
        }
    }

    override fun registerPayload(registrar: PayloadRegistrar) {
        registrar.playToClient(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC, HTPayloadHandlers::handleS2C)
        registrar.playBidirectional(HTUpdateMenuPacket.TYPE, HTUpdateMenuPacket.STREAM_CODEC, HTPayloadHandlers::handleS2C, HTPayloadHandlers::handleC2S)
    }
}
