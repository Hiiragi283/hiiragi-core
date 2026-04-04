package hiiragi283.core.client.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import net.minecraft.world.item.alchemy.PotionContents
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.FluidTooltipEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = HiiragiCoreAPI.MOD_ID)
object HCClientEventHandlers {
    @SubscribeEvent
    fun addFluidTooltips(event: FluidTooltipEvent) {
        val (contents: PotionContents, _) = HTPotionHelper.getContents(event.fluidStack) ?: return
        contents.addToTooltip(event.context, event.toolTip::add, event.flags, event.fluidStack)
    }
}
