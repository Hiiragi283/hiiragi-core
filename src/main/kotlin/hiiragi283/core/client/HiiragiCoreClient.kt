package hiiragi283.core.client

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTRegisterWidgetRendererEvent
import hiiragi283.core.api.mod.HTClientMod
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.toId
import hiiragi283.core.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.client.gui.widget.HTEmptyWidgetRenderer
import hiiragi283.core.client.gui.widget.HTFluidTankWidgetRenderer
import hiiragi283.core.client.gui.widget.HTWidgetRendererManager
import hiiragi283.core.common.item.HTChromaticPowderItem
import hiiragi283.core.setup.HCEntityTypes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCMenuTypes
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.world.item.ItemStack
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
import java.awt.Color

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
        event.register(HCWidgetTypes.FLUID_TANK.get(), ::HTFluidTankWidgetRenderer)
        event.register(HCWidgetTypes.ITEM_SLOT.get(), HTEmptyWidgetRenderer::create)
    }

    override fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
        // Chromatic Powder
        event.register(
            { stack: ItemStack, tint: Int ->
                when (tint) {
                    0 -> HTChromaticPowderItem.getColor(stack)
                    else -> -1
                }
            },
            HCItems.CHROMATIC_POWDER,
        )
        // Bucket
        val bucketColor = DynamicFluidContainerModel.Colors()
        for (item: ItemLike in HCFluids.REGISTER.asItemSequence()) {
            event.register(bucketColor, item)
        }
    }

    override fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        // Vanilla
        event.clear(HCFluids.EXPERIENCE, Color(0x66ff33))
        event.registerFluidType(
            HTSimpleFluidExtensions(HTConst.MINECRAFT.toId(HTConst.BLOCK, "honey_block_top")),
            HCFluids.HONEY.getFluidType(),
        )
        event.dull(HCFluids.MUSHROOM_STEW, Color(0xcc9966))
        // Saps
        event.dull(HCFluids.LATEX, Color(0xcccccc))
        event.dull(HCFluids.BLOOD, Color(0x990000))
        event.dull(HCFluids.MEAT, Color(0x993333))

        event.molten(HCFluids.MOLTEN_GLASS, Color(0xe6e6e6))
        event.molten(HCFluids.MOLTEN_PLASTIC, Color(0xa0cfb5))
        event.molten(HCFluids.MOLTEN_RUBBER, Color(0x453945))
        event.molten(HCFluids.MOLTEN_CRIMSON_CRYSTAL, Color(0x993333))
        event.molten(HCFluids.MOLTEN_WARPED_CRYSTAL, Color(0x339999))
        event.molten(HCFluids.MOLTEN_ELDRITCH, Color(0x6633cc))
    }

    override fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(HCMenuTypes.BLOCK.get(), ::HTWidgetContainerScreen)
        event.register(HCMenuTypes.ITEM.get(), ::HTWidgetContainerScreen)
    }

    override fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        // Entity
        event.registerEntityRenderer(HCEntityTypes.ELDRITCH_EGG.get(), ::ThrownItemRenderer)
    }

    //    Extensions    //

    private fun RegisterClientExtensionsEvent.clear(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.clear(color), content.getFluidType())
    }

    private fun RegisterClientExtensionsEvent.dull(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.dull(color), content.getFluidType())
    }

    private fun RegisterClientExtensionsEvent.molten(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.molten(color), content.getFluidType())
    }
}
