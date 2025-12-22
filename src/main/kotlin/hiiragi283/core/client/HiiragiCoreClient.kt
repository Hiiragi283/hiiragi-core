package hiiragi283.core.client

import hiiragi283.core.HiiragiCore
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.setup.HCFluids
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.awt.Color

@Mod(value = HiiragiCoreAPI.MOD_ID, dist = [Dist.CLIENT])
object HiiragiCoreClient {
    init {
        val eventBus: IEventBus = MOD_BUS

        eventBus.addListener(::registerItemColors)
        eventBus.addListener(::registerClientExtensions)

        HiiragiCore.LOGGER.info("Hiiragi-Core loaded on client side!")
    }

    @JvmStatic
    private fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
        val bucketColor = DynamicFluidContainerModel.Colors()
        for (item: ItemLike in HCFluids.REGISTER.asItemSequence()) {
            event.register(bucketColor, item)
        }
        HiiragiCore.LOGGER.info("Registered item colors!")
    }

    @JvmStatic
    private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        // Vanilla
        event.registerFluidType(
            HTSimpleFluidExtensions(vanillaId("block", "honey_block_top")),
            HCFluids.HONEY.getFluidType(),
        )
        event.dull(HCFluids.MUSHROOM_STEW, Color(0xcc9966))
        // Saps
        event.dull(HCFluids.LATEX, Color(0xcccccc))
        event.dull(HCFluids.CRIMSON_SAP, Color(0x660000))
        event.dull(HCFluids.WARPED_SAP, Color(0x006666))

        event.molten(HCFluids.CRIMSON_BLOOD, Color(0x990000))
        event.molten(HCFluids.DEW_OF_THE_WARP, Color(0x009999))
        event.molten(HCFluids.ELDRITCH_FLUX, Color(0x990099))

        HiiragiCore.LOGGER.info("Registered client extensions!")
    }

    //    Extensions    //

    private fun RegisterClientExtensionsEvent.clear(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(
            HTSimpleFluidExtensions(
                vanillaId("block", "water_still"),
                color,
                vanillaId("block", "water_flow"),
            ),
            content.getFluidType(),
        )
    }

    private fun RegisterClientExtensionsEvent.dull(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(
            HTSimpleFluidExtensions(
                HTConst.NEOFORGE.toId("block", "milk_still"),
                color,
                HTConst.NEOFORGE.toId("block", "milk_flowing"),
            ),
            content.getFluidType(),
        )
    }

    private fun RegisterClientExtensionsEvent.molten(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(
            HTSimpleFluidExtensions(
                HiiragiCoreAPI.id("block", "molten_still"),
                color,
                HiiragiCoreAPI.id("block", "molten_flow"),
            ),
            content.getFluidType(),
        )
    }
}
