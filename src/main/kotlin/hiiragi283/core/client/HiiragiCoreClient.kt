package hiiragi283.core.client

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.common.material.HCMoltenCrystalData
import hiiragi283.core.setup.HCFluids
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.awt.Color

@Mod(value = HiiragiCoreAPI.MOD_ID, dist = [Dist.CLIENT])
object HiiragiCoreClient {
    init {
        val eventBus: IEventBus = MOD_BUS

        eventBus.addListener(::registerClientExtensions)
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

        for (data: HCMoltenCrystalData in HCMoltenCrystalData.entries) {
            val color: Color = data.color
            // molten
            event.molten(data.molten, color)
            // sap
            val sap: HTSimpleFluidContent = data.sap ?: continue
            event.dull(sap, color)
        }
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
