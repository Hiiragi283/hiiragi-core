package hiiragi283.core.client.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.setup.HCDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import java.util.function.Consumer

@EventBusSubscriber(value = [Dist.CLIENT], modid = HiiragiCoreAPI.MOD_ID)
object RagiumTooltipHandler {
    @SubscribeEvent
    fun onItemTooltip(event: ItemTooltipEvent) {
        val stack: ItemStack = event.itemStack
        val context: Item.TooltipContext = event.context
        val consumer: Consumer<Component> = Consumer { event.toolTip.add(1, it) }
        val consumer1: Consumer<Component> = Consumer(event.toolTip::add)
        val flag: TooltipFlag = event.flags

        information(stack, consumer, flag)

        HCDataComponents.REGISTER
            .asSequence()
            .mapNotNull(stack::get)
            .filterIsInstance<TooltipProvider>()
            .forEach { provider: TooltipProvider -> provider.addToTooltip(context, consumer1, flag) }
    }

    @JvmStatic
    private fun information(stack: ItemStack, consumer: Consumer<Component>, flag: TooltipFlag) {
        val translation: HTTranslation = stack.get(HCDataComponents.DESCRIPTION) ?: return
        if (flag.hasShiftDown()) {
            consumer.accept(translation.translate())
        } else {
            consumer.accept(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(ChatFormatting.YELLOW))
        }
    }
}
