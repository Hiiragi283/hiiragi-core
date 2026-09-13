package hiiragi283.core.client

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.data.pack.HTDynamicResourcePack
import hiiragi283.core.api.event.HTRegisterWidgetRendererEvent
import hiiragi283.core.api.mod.HTClientMod
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.client.data.HCDynamicClientResources
import hiiragi283.core.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.client.gui.widget.HTFluidWidgetRenderer
import hiiragi283.core.client.gui.widget.HTItemWidgetRenderer
import hiiragi283.core.client.gui.widget.HTProgressWidgetRenderer
import hiiragi283.core.client.render.block.HTCopperBasinRenderer
import hiiragi283.core.common.data.pack.HTPackSource
import hiiragi283.core.internal.gui.widget.HTWidgetRendererManager
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCEntityTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCWidgetTypes
import hiiragi283.core.support.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.core.support.gui.factory.HTItemWidgetHolderContext
import java.awt.Color
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.repository.Pack
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import net.neoforged.neoforge.event.AddPackFindersEvent

@Mod(value = HiiragiCoreAPI.MOD_ID, dist = [Dist.CLIENT])
data object HiiragiCoreClient : HTClientMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        configScreen(container)

        HiiragiCoreAPI.LOGGER.info("Hiiragi-Core loaded on client side")
    }

    override fun clientSetup(event: FMLClientSetupEvent) {
        HTWidgetRendererManager.init()
    }

    override fun registerWidgetRenderer(event: HTRegisterWidgetRendererEvent) {
        event.register(HCWidgetTypes.FLUID, ::HTFluidWidgetRenderer)
        event.register(HCWidgetTypes.ITEM, ::HTItemWidgetRenderer)
        event.register(HCWidgetTypes.PROGRESS, ::HTProgressWidgetRenderer)
    }

    override fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
        // Bucket
        val bucketColor = DynamicFluidContainerModel.Colors()
        for (item: ItemLike in HCFluids.REGISTER.asItemSequence()) {
            event.register(bucketColor, item)
        }
    }

    override fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        // Vanilla
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DYES.asSequenceWithColor()) {
            event.dull(content, Color(color.color))
        }

        event.clear(HCFluids.EXPERIENCE, Color(0x66ff33))
        event.registerFluidType(
            HTSimpleFluidExtensions(vanillaId(HTConst.BLOCK, "honey_block_top")),
            HCFluids.HONEY.getFluidType(),
        )
        event.dull(HCFluids.MUSHROOM_STEW, Color(0xcc9966))
        event.registerFluidType(
            HTSimpleFluidExtensions(HiiragiCoreAPI.id(HTConst.BLOCK, "dragon_breath")),
            HCFluids.DRAGON_BREATH.getFluidType(),
        )
        event.registerFluidType(HTPotionFluidExtensions, HCFluids.POTION.getFluidType())
        event.molten(HCFluids.OMINOUS_FLUX, Color(0x003366))

        event.dull(HCFluids.LATEX, Color(0xcccccc))
        event.dull(HCFluids.MEAT, Color(0x993333))

        event.molten(HCFluids.MOLTEN_GLASS, Color(0xffffff))
        event.molten(HCFluids.MOLTEN_ENDER, Color(0x006666))
        event.molten(HCFluids.MOLTEN_BLAZE, Color(0xcc9900))

        event.molten(HCFluids.MOLTEN_CRIMSON_CRYSTAL, Color(0xcc3333))
        event.molten(HCFluids.MOLTEN_WARPED_CRYSTAL, Color(0x33cccc))
        event.molten(HCFluids.MOLTEN_ELDRITCH, Color(0x6633cc))

        /*for (fluid: HTMaterialContents.FluidEntry in HiiragiCoreAccess.INSTANCE.registeredFluids.values) {
            event.registerFluidType(HTSimpleFluidExtensions(fluid.blockId), fluid.get().fluidType)
        }*/
    }

    override fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(HTBlockWidgetHolderContext.MENU_TYPE.get(), ::HTWidgetContainerScreen)
        event.register(HTItemWidgetHolderContext.MENU_TYPE.get(), ::HTWidgetContainerScreen)
    }

    override fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        // Block Entity
        event.registerBlockEntityRenderer(HCBlockEntityTypes.COPPER_BASIN.get(), ::HTCopperBasinRenderer)
        // Entity
        event.registerEntityRenderer(HCEntityTypes.BOMB.get(), ::ThrownItemRenderer)
        event.registerEntityRenderer(HCEntityTypes.ELDRITCH_EGG.get(), ::ThrownItemRenderer)
    }

    override fun registerPack(event: AddPackFindersEvent) {
        val packType: PackType = event.packType
        if (packType == PackType.CLIENT_RESOURCES) {
            HTDynamicResourcePack.clear()

            HCDynamicClientResources.initialize()
            HiiragiCoreAccess.INSTANCE.forEachPlugin("Registering Client Resources", HTMaterialPlugin::registerClientResources)

            event.addRepositorySource(
                HTPackSource(
                    HiiragiCoreAPI.id("asset").toString(),
                    packType,
                    Pack.Position.TOP,
                    ::HTDynamicResourcePack,
                ),
            )

            HiiragiCoreAPI.LOGGER.info("Added dynamic resource pack")
        }
    }

    //    Extensions    //

    private fun RegisterClientExtensionsEvent.clear(content: HTFluidContent, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.clear(color), content.getFluidType())
    }

    private fun RegisterClientExtensionsEvent.dull(content: HTFluidContent, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.dull(color), content.getFluidType())
    }

    private fun RegisterClientExtensionsEvent.molten(content: HTFluidContent, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.molten(color), content.getFluidType())
    }
}
