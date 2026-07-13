package hiiragi283.core.client.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.api.text.Text
import hiiragi283.core.setup.HCDataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import java.util.function.Consumer

@EventBusSubscriber(value = [Dist.CLIENT], modid = HiiragiCoreAPI.MOD_ID)
data object HCTooltipHandler {
    @SubscribeEvent
    fun onItemTooltip(event: ItemTooltipEvent) {
        val stack: ItemStack = event.itemStack
        val consumer: Consumer<Text> = Consumer { event.toolTip.add(1, it) }
        val flag: TooltipFlag = event.flags

        information(stack, consumer, flag)
    }

    @JvmStatic
    private fun information(stack: ItemStack, consumer: Consumer<Text>, flag: TooltipFlag) {
        val translation: HTTranslation = stack.get(HCDataComponents.DESCRIPTION) ?: return
        if (flag.hasShiftDown()) {
            consumer.accept(translation.translate())
        } else {
            consumer.accept(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(HTDefaultColor.YELLOW))
        }
    }
}
