package hiiragi283.core

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.mod.HTCommonMod
import hiiragi283.core.api.network.HTPayloadHandlers
import hiiragi283.core.common.network.HTUpdateBlockEntityPacket
import hiiragi283.core.common.network.HTUpdateMenuPacket
import hiiragi283.core.config.HCConfig
import hiiragi283.core.setup.HCAttachmentTypes
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCCreativeTabs
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCEntityTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCMiscRegister
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.core.setup.HCWidgetTypes
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.NewRegistryEvent
import org.slf4j.Logger

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCore : HTCommonMod() {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        eventBus.addListener(HCMiscRegister::register)

        HCDataComponents.REGISTER.register(eventBus)

        HCEntityTypes.register(eventBus)
        HCFluids.register(eventBus)
        HCBlocks.register(eventBus)
        HCItems.register(eventBus)

        HCAttachmentTypes.REGISTER.register(eventBus)
        HCBlockEntityTypes.register(eventBus)
        HCCreativeTabs.REGISTER.register(eventBus)
        HCRecipeSerializers.REGISTER.register(eventBus)
        HCRecipeTypes.REGISTER.register(eventBus)
        HCWidgetTypes.REGISTER.register(eventBus)

        container.registerConfig(ModConfig.Type.COMMON, HCConfig.COMMON_SPEC)

        LOGGER.info("Hiiragi-Core loaded")
    }

    override fun registerRegistries(event: NewRegistryEvent) {
        event.register(HCRegistries.SLOT_TYPE)
        event.register(HCRegistries.WIDGET_TYPE)

        LOGGER.info("Registered new registries")
    }

    override fun registerPayload(registrar: PayloadRegistrar) {
        registrar.playToClient(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC, HTPayloadHandlers::handleS2C)
        registrar.playBidirectional(HTUpdateMenuPacket.TYPE, HTUpdateMenuPacket.STREAM_CODEC, HTPayloadHandlers::handleBoth)

        LOGGER.info("Registered payload handlers")
    }
}
