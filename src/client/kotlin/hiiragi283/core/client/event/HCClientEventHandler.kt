package hiiragi283.core.client.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCFluids
import hiiragi283.lib.item.alchemy.HTPotionHelper
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.FluidTooltipEvent
import net.neoforged.neoforge.fluids.FluidStack

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
data object HCClientEventHandler {
    @SubscribeEvent
    fun onFluidTooltip(event: FluidTooltipEvent) {
        val stack: FluidStack = event.fluidStack
        if (stack.`is`(HCFluids.POTION.get())) {
            HTPotionHelper.getPotion(stack).addToTooltip(event.context, event.toolTip::add, event.flags, stack)
        }
    }
}
