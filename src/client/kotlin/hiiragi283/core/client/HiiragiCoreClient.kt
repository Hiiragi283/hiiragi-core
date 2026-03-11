package hiiragi283.core.client

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.event.HTRegisterWidgetRendererEvent
import hiiragi283.core.api.mod.HTClientMod
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.getFluidType
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.client.data.HCClientResourceProvider
import hiiragi283.core.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.client.gui.tooltip.HTClientFluidFilterTooltip
import hiiragi283.core.client.gui.tooltip.HTClientItemFilterTooltip
import hiiragi283.core.client.gui.widget.HTFluidWidgetRenderer
import hiiragi283.core.client.gui.widget.HTItemSlotWidgetRenderer
import hiiragi283.core.client.gui.widget.HTProgressWidgetRenderer
import hiiragi283.core.client.gui.widget.HTWidgetRendererManager
import hiiragi283.core.common.gui.tooltip.HTFluidFilterTooltip
import hiiragi283.core.common.gui.tooltip.HTItemFilterTooltip
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCEntityTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCMenuTypes
import hiiragi283.core.setup.HCWidgetTypes
import net.mehvahdjukaar.moonlight.api.platform.RegHelper
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import java.awt.Color

@Mod(value = HiiragiCoreAPI.MOD_ID, dist = [Dist.CLIENT])
data object HiiragiCoreClient : HTClientMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        eventBus.addListener(::registerClientTooltips)

        configScreen(container)

        RegHelper.registerDynamicResourceProvider(HCClientResourceProvider)
        HiiragiCoreAPI.LOGGER.info("Hiiragi-Core loaded on client side")
    }

    private fun registerClientTooltips(event: RegisterClientTooltipComponentFactoriesEvent) {
        event.register(HTFluidFilterTooltip::class.java, ::HTClientFluidFilterTooltip)
        event.register(HTItemFilterTooltip::class.java, ::HTClientItemFilterTooltip)
    }

    override fun clientSetup(event: FMLClientSetupEvent) {
        HTWidgetRendererManager.init()
    }

    override fun registerWidgetRenderer(event: HTRegisterWidgetRendererEvent) {
        event.register(HCWidgetTypes.FLUID.get(), ::HTFluidWidgetRenderer)
        event.register(HCWidgetTypes.ITEM_SLOT.get(), ::HTItemSlotWidgetRenderer)
        event.register(HCWidgetTypes.PROGRESS.get(), ::HTProgressWidgetRenderer)
    }

    override fun registerBlockColors(event: RegisterColorHandlersEvent.Block) {
        // Latex Cauldron
        event.register(
            { _: BlockState, _: BlockAndTintGetter?, _: BlockPos?, _: Int ->
                IClientFluidTypeExtensions.of(HCFluids.LATEX.get()).tintColor
            },
            HCBlocks.LATEX_CAULDRON.get(),
        )
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
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DYE.entries) {
            event.dull(content, Color(color.color))
        }

        event.clear(HCFluids.EXPERIENCE, Color(0x66ff33))
        event.registerFluidType(
            HTSimpleFluidExtensions(HTConst.MINECRAFT.toId(HTConst.BLOCK, "honey_block_top")),
            HCFluids.HONEY.getFluidType(),
        )
        event.dull(HCFluids.MUSHROOM_STEW, Color(0xcc9966))
        event.registerFluidType(
            HTSimpleFluidExtensions(HiiragiCoreAPI.id(HTConst.BLOCK, "dragon_breath")),
            HCFluids.DRAGON_BREATH.getFluidType(),
        )
        event.registerFluidType(HTPotionFluidExtensions, HCFluids.POTION.getFluidType())
        event.dull(HCFluids.OMINOUS_FLUX, Color(0x003366))

        event.dull(HCFluids.LATEX, Color(0xcccccc))
        event.dull(HCFluids.MEAT, Color(0x993333))

        for (holder: HTFluidHolderLike<*> in HiiragiCoreAccess.INSTANCE.registeredFluids.values) {
            event.registerFluidType(HTSimpleFluidExtensions(holder.blockId), holder.getFluidType())
        }
    }

    override fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(HCMenuTypes.BLOCK.get(), ::HTWidgetContainerScreen)
        event.register(HCMenuTypes.ITEM.get(), ::HTWidgetContainerScreen)
    }

    override fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        // Entity
        event.registerEntityRenderer(HCEntityTypes.BOMB.get(), ::ThrownItemRenderer)
        event.registerEntityRenderer(HCEntityTypes.ELDRITCH_EGG.get(), ::ThrownItemRenderer)
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
