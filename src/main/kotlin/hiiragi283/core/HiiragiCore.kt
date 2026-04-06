package hiiragi283.core

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import hiiragi283.core.api.mod.HTCommonMod
import hiiragi283.core.api.network.HTPayloadHandlers
import hiiragi283.core.common.block.dispenser.HCDispenserBehaviours
import hiiragi283.core.common.data.HCServerResourceProvider
import hiiragi283.core.common.network.HTUpdateBlockEntityPacket
import hiiragi283.core.common.network.HTUpdateMenuPacket
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.config.HCConfig
import hiiragi283.core.impl.HiiragiCoreAccessImpl
import hiiragi283.core.setup.HCAttachmentTypes
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCCreativeTabs
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCEntityTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCMenuTypes
import hiiragi283.core.setup.HCMiscRegister
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.core.setup.HCWidgetTypes
import net.mehvahdjukaar.moonlight.api.platform.RegHelper
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.DispenserBlock
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.NewRegistryEvent

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCore : HTCommonMod() {
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
        HCMenuTypes.REGISTER.register(eventBus)
        HCRecipeSerializers.REGISTER.register(eventBus)
        HCRecipeTypes.REGISTER.register(eventBus)
        HCWidgetTypes.REGISTER.register(eventBus)

        container.registerConfig(ModConfig.Type.COMMON, HCConfig.COMMON_SPEC)

        RegHelper.registerDynamicResourceProvider(HCServerResourceProvider)

        HiiragiCoreAPI.LOGGER.info("Hiiragi-Core loaded")
    }

    override fun registerRegistries(event: NewRegistryEvent) {
        event.register(HCRegistries.SLOT_TYPE)
        event.register(HCRegistries.TANK_INTERACTION_TYPE)
        event.register(HCRegistries.WIDGET_TYPE)
    }

    override fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            HCItems.REGISTER
                .asItemSequence()
                .map(ItemLike::asItem)
                .filter { it is ProjectileItem }
                .forEach(DispenserBlock::registerProjectileBehavior)
        }

        event.enqueueWork(::registerPotionHandlers)
        event.enqueueWork(HCDispenserBehaviours::init)
        event.enqueueWork(HCRecipeLookups::init)
    }

    private fun registerPotionHandlers() {
        // Potion Fluid
        HTPotionFluidManager.register(HCFluids.POTION.get(), HiiragiCoreAccessImpl.DEFAULT_POTION_HANDLER)
    }

    override fun registerPayload(registrar: PayloadRegistrar) {
        registrar.playToClient(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC, HTPayloadHandlers::handleS2C)
        registrar.playBidirectional(HTUpdateMenuPacket.TYPE, HTUpdateMenuPacket.STREAM_CODEC, HTPayloadHandlers::handleBoth)
    }
}
