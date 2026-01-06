package hiiragi283.core.util

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextUtil
import hiiragi283.core.api.text.toText
import hiiragi283.core.api.text.withStyle
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
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
        stack: FluidStack,
        consumer: Consumer<Component>,
        flag: TooltipFlag,
        isCreative: Boolean,
    ) {
        // Empty name if stack is empty
        if (stack.isEmpty) {
            consumer.accept(HTCommonTranslation.EMPTY.translate())
            return
        }
        // Fluid Name and Amount
        if (isCreative) {
            HTCommonTranslation.STORED.translate(stack, HTCommonTranslation.INFINITE)
        } else {
            HTCommonTranslation.STORED_MB.translate(stack, stack.amount)
        }.let(consumer::accept)
        // Fluid id if advanced
        val holder: Holder<Fluid> = stack.fluidHolder
        if (flag.isAdvanced) {
            holder
                .registeredName
                .toText()
                .withStyle(HTDefaultColor.GRAY)
                .let(consumer::accept)
        }
        // Mod Name
        holder
            .toLike()
            .getId()
            .namespace
            .let(HTTextUtil::getModNameText)
            .withStyle(HTDefaultColor.BLUE)
            .withStyle(ChatFormatting.ITALIC)
            .let(consumer::accept)
    }
}
