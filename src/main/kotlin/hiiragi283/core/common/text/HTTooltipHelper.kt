package hiiragi283.core.common.text

import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextUtil
import hiiragi283.core.api.text.toText
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import java.util.function.Consumer

object HTTooltipHelper {
    @JvmStatic
    fun addEnergyTooltip(amount: Int, consumer: Consumer<Component>, isCreative: Boolean) {
        // Empty name if amount is not positive
        if (amount <= 0) {
            consumer.accept(HTCommonTranslation.EMPTY.translate())
            return
        }
        // Fluid Name and Amount
        if (isCreative) {
            HTCommonTranslation.STORED_FE.translate(HTCommonTranslation.INFINITE)
        } else {
            HTCommonTranslation.STORED_FE.translate(amount)
        }.let(consumer::accept)
    }

    @JvmStatic
    fun addFluidTooltip(
        stack: ImmutableFluidStack?,
        consumer: Consumer<Component>,
        flag: TooltipFlag,
        isCreative: Boolean,
    ) {
        // Empty name if stack is empty
        if (stack == null) {
            consumer.accept(HTCommonTranslation.EMPTY.translate())
            return
        }
        // Fluid Name and Amount
        if (isCreative) {
            HTCommonTranslation.STORED.translate(stack, HTCommonTranslation.INFINITE)
        } else {
            HTCommonTranslation.STORED_MB.translate(stack, stack.amount())
        }.let(consumer::accept)
        // Fluid id if advanced
        if (flag.isAdvanced) {
            stack
                .getHolder()
                .registeredName
                .toText()
                .withStyle(ChatFormatting.DARK_GRAY)
                .let(consumer::accept)
        }
        // Mod Name
        stack
            .getId()
            .namespace
            .let(HTTextUtil::getModNameText)
            .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC)
            .let(consumer::accept)
    }
}
