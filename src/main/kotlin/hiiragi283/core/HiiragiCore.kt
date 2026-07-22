package hiiragi283.core

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.pack.HTDynamicDatapack
import hiiragi283.core.api.item.alchemy.HTPotionFluidManager
import hiiragi283.core.api.mod.HTCommonMod
import hiiragi283.core.api.text.toText
import hiiragi283.core.common.block.dispenser.HCDispenserBehaviours
import hiiragi283.core.common.data.pack.HTPackSource
import hiiragi283.core.common.item.HTPotionBucketItem
import hiiragi283.core.common.network.HTUpdateBlockEntityPacket
import hiiragi283.core.common.network.HTUpdateMenuPacket
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.storage.fluid.HTBasicItemFluidTank
import hiiragi283.core.common.storage.fluid.HTExperienceTomeFluidTank
import hiiragi283.core.config.HCConfig
import hiiragi283.core.internal.HiiragiCoreAccessImpl
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCEntityTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCMiscRegister
import hiiragi283.core.support.capability.HTFluidCapabilities
import hiiragi283.core.support.network.HTPayloadHandlers
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.repository.Pack
import net.minecraft.server.packs.repository.PackSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.DispenserBlock
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.event.AddPackFindersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.NewRegistryEvent

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCore : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        eventBus.addListener(HCMiscRegister::register)

        HCEntityTypes.REGISTER.register(eventBus)
        HCFluids.register(eventBus)
        HCBlocks.register(eventBus)
        HCItems.register(eventBus)

        HCBlockEntityTypes.REGISTER.register(eventBus)

        container.registerConfig(ModConfig.Type.COMMON, HCConfig.COMMON_SPEC)

        HiiragiCoreAPI.LOGGER.info("Hiiragi-Core loaded")
    }

    override fun registerRegistries(event: NewRegistryEvent) {
        event.register(HCRegistries.ITEM_RESULT_SERIALIZER)
        event.register(HCRegistries.SLOT_TYPE)
        event.register(HCRegistries.WIDGET_TYPE)
    }

    override fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            HCItems.REGISTER
                .asSequence()
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

    override fun registerCapabilities(helper: CapabilityHelper) {
        // Block
        helper.registerBlockEntity(HCBlockEntityTypes.COPPER_BASIN.get())

        helper.registerBlockEntity(HCBlockEntityTypes.TEST.get())
        // Item
        helper.registerItem(HTFluidCapabilities, HTPotionBucketItem::BucketHandler, HCFluids.POTION.bucketHolder)
        helper.registerItemTank({ container: ItemStack -> HTBasicItemFluidTank.create(container, 4000) }, HCItems.PAINT_BRUSH)
        helper.registerItemTank(::HTExperienceTomeFluidTank, HCItems.EXPERIENCE_TOME)
    }

    override fun registerPayload(registrar: PayloadRegistrar) {
        registrar.playToClient(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC, HTPayloadHandlers::handleS2C)
        registrar.playBidirectional(HTUpdateMenuPacket.TYPE, HTUpdateMenuPacket.STREAM_CODEC, HTPayloadHandlers::handleBoth)
    }

    override fun registerPack(event: AddPackFindersEvent) {
        val packType: PackType = event.packType
        when (packType) {
            PackType.CLIENT_RESOURCES -> {
            }
            PackType.SERVER_DATA -> {
            }
        }
        if (packType == PackType.SERVER_DATA) {
            if (!FMLEnvironment.production) {
                event.addPackFinders(
                    HiiragiCoreAPI.id("data", HiiragiCoreAPI.MOD_ID, "datapacks", HTConst.EXPERIMENTAL),
                    packType,
                    "Hiiragi Core: Experimental".toText(),
                    PackSource.FEATURE,
                    false,
                    Pack.Position.TOP,
                )
                HiiragiCoreAPI.LOGGER.info("Enabled Experimental Feature")
            }

            HTDynamicDatapack.clear()

            event.addRepositorySource(
                HTPackSource(
                    HiiragiCoreAPI.id("data").toString(),
                    packType,
                    Pack.Position.TOP,
                    ::HTDynamicDatapack,
                ),
            )
            HiiragiCoreAPI.LOGGER.info("Added dynamic datapack")
        }
    }
}
