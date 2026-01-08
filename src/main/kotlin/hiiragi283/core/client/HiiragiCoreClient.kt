package hiiragi283.core.client

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCEntityTypes
import hiiragi283.core.setup.HCFluids
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.awt.Color

@Mod(value = HiiragiCoreAPI.MOD_ID, dist = [Dist.CLIENT])
object HiiragiCoreClient {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    init {
        val eventBus: IEventBus = MOD_BUS

        eventBus.addListener(::registerItemColors)
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::registerEntityRenderer)

        LOADING_CONTEXT.activeContainer
            .registerExtensionPoint(IConfigScreenFactory::class.java, IConfigScreenFactory(::ConfigurationScreen))

        LOGGER.info("Hiiragi-Core loaded on client side!")
    }

    @JvmStatic
    private fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
        val bucketColor = DynamicFluidContainerModel.Colors()
        for (item: ItemLike in HCFluids.REGISTER.asItemSequence()) {
            event.register(bucketColor, item)
        }
        LOGGER.info("Registered item colors!")
    }

    @JvmStatic
    private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        // Vanilla
        event.clear(HCFluids.EXPERIENCE, Color(0x66ff33))
        event.registerFluidType(
            HTSimpleFluidExtensions(vanillaId(HTConst.BLOCK, "honey_block_top")),
            HCFluids.HONEY.getFluidType(),
        )
        event.dull(HCFluids.MUSHROOM_STEW, Color(0xcc9966))
        // Saps
        event.dull(HCFluids.LATEX, Color(0xcccccc))
        event.dull(HCFluids.BLOOD, Color(0x990000))
        event.dull(HCFluids.MEAT, Color(0x993333))

        fun molten(key: HTMaterialKey, color: Color) {
            val content: HTSimpleFluidContent = HCFluids.MATERIALS[HCMaterialPrefixes.MOLTEN, key] ?: return
            event.molten(content, color)
        }

        molten(VanillaMaterialKeys.COPPER, Color(0xe77c56))
        molten(VanillaMaterialKeys.IRON, Color(0x838383))
        molten(VanillaMaterialKeys.GOLD, Color(0xeccb45))
        molten(VanillaMaterialKeys.NETHERITE, Color(0x3b393b))
        molten(VanillaMaterialKeys.GLASS, Color(0xccccff))

        molten(CommonMaterialKeys.STEEL, Color(0x525252))

        molten(CommonMaterialKeys.PLASTIC, Color(0x9abeba))
        molten(CommonMaterialKeys.RUBBER, Color(0x2e262e))

        molten(HCMaterialKeys.CRIMSON_CRYSTAL, Color(0x993333))
        molten(HCMaterialKeys.WARPED_CRYSTAL, Color(0x339999))
        molten(HCMaterialKeys.ELDRITCH, Color(0x6633cc))

        molten(HCMaterialKeys.AZURE_STEEL, Color(0x6666cc))
        molten(HCMaterialKeys.DEEP_STEEL, Color(0x669999))

        LOGGER.info("Registered client extensions!")
    }

    private fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
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
