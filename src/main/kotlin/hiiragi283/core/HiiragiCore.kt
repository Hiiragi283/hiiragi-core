package hiiragi283.core

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.network.HTPayloadHandlers
import hiiragi283.core.common.network.HTUpdateBlockEntityPacket
import hiiragi283.core.common.network.HTUpdateMenuPacket
import hiiragi283.core.config.HCConfig
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCCreativeTabs
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCMiscRegister
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.NewRegistryEvent
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCore {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    init {
        NeoForgeMod.enableMilkFluid()

        val eventBus: IEventBus = MOD_BUS
        val container: ModContainer = LOADING_CONTEXT.activeContainer

        eventBus.addListener(::registerRegistries)
        eventBus.addListener(HCMiscRegister::register)
        eventBus.addListener(::commonSetup)
        eventBus.addListener { event: RegisterPayloadHandlersEvent ->
            container.modInfo.version
                .toString()
                .let(event::registrar)
                .let(::registerPayload)
        }

        HCDataComponents.REGISTER.register(eventBus)

        HCFluids.register(eventBus)
        HCBlocks.register(eventBus)
        HCItems.register(eventBus)

        HCCreativeTabs.REGISTER.register(eventBus)
        HCRecipeSerializers.REGISTER.register(eventBus)
        HCRecipeTypes.REGISTER.register(eventBus)

        container.registerConfig(ModConfig.Type.COMMON, HCConfig.COMMON_SPEC)

        LOGGER.info("Hiiragi-Core loaded!")
    }

    private fun registerRegistries(event: NewRegistryEvent) {
        event.register(HiiragiCoreAPI.SLOT_TYPE_REGISTRY)

        LOGGER.info("Registered new registries!")
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
    }

    private fun registerPayload(registrar: PayloadRegistrar) {
        registrar.playToClient(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC, HTPayloadHandlers::handleS2C)
        registrar.playBidirectional(HTUpdateMenuPacket.TYPE, HTUpdateMenuPacket.STREAM_CODEC, HTPayloadHandlers::handleBoth)
    }
}
