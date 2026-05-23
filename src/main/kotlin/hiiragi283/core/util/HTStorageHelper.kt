package hiiragi283.core.util

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.fixedFraction
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextUtil
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.toText
import hiiragi283.core.api.text.withStyle
import hiiragi283.core.common.capability.HTEnergyCapabilities
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.setup.HCDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.redstone.Redstone
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.SimpleFluidContent
import java.util.function.Consumer
import kotlin.math.roundToInt

/**
 * @see mekanism.common.util.StorageUtils
 */
data object HTStorageHelper {
    //    Amount    //

    /**
     * @see net.neoforged.neoforge.items.ItemHandlerHelper.calcRedstoneFromInventory
     * @see mekanism.common.util.MekanismUtils.redstoneLevelFromContents
     */
    @JvmStatic
    fun calculateRedstoneLevel(views: Iterable<HTAmountView>): Int {
        var amountSum = 0
        var capacitySum = 0
        for (view: HTAmountView in views) {
            amountSum += view.getAmount()
            capacitySum += view.getCapacity()
        }
        return calculateRedstoneLevel(amountSum, capacitySum)
    }

    /**
     * @see mekanism.common.util.MekanismUtils.redstoneLevelFromContents
     */
    @JvmStatic
    fun calculateRedstoneLevel(amount: Int, capacity: Int): Int = Mth.lerpDiscrete(fixedFraction(amount, capacity).toFloat(), Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)

    @JvmStatic
    fun calculateRedstoneLevel(view: HTAmountView): Int = Mth.lerpDiscrete(view.getLevelAsFloat(), Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)

    //    Energy    //

    @JvmStatic
    fun createStackWithEnergy(
        item: ItemLike,
        amount: Int,
        count: Int = 1,
        patch: DataComponentPatch = DataComponentPatch.EMPTY,
    ): ItemStack {
        val stack: ItemStack = createItemStack(item, count, patch)
        updateEnergy(stack, amount)
        return stack
    }

    @JvmStatic
    fun getEnergy(container: ItemStack): Int = container.getOrDefault(HCDataComponents.ENERGY, 0)

    @JvmStatic
    fun updateEnergy(container: ItemStack, newAmount: Int) {
        if (newAmount <= 0) {
            container.remove(HCDataComponents.ENERGY)
        } else {
            container.set(HCDataComponents.ENERGY, newAmount)
        }
    }

    @JvmStatic
    fun addEnergyTooltip(view: HTAmountView, consumer: Consumer<Text>, isCreative: Boolean) {
        // Empty name if amount is not positive
        if (view.getAmount() <= 0) {
            consumer.accept(HTCommonTranslation.EMPTY.translate())
            return
        }
        // Fluid Name and Amount
        when {
            isCreative -> HTCommonTranslation.STORED_FE.translate(HTCommonTranslation.INFINITE)
            else -> HTCommonTranslation.STORED_FE.translate(HTCommonTranslation.FRACTION.translate(view.getAmount(), view.getCapacity()))
        }.let(consumer::accept)
    }

    @JvmStatic
    fun getEnergyBarWidth(container: ItemStack): Int = when {
        container.count > 1 -> 0
        else -> (13.0 - 13.0 * getEnergyDurability(container)).roundToInt()
    }

    @JvmStatic
    private fun getEnergyDurability(container: ItemStack): Double {
        val bestRatio: Double = HTEnergyCapabilities
            .getBattery(container)
            ?.getLevelAsFraction()
            ?.toDouble()
            ?: 0.0
        return 1 - bestRatio
    }

    //    Fluid    //

    @JvmStatic
    fun createStackWithFluid(
        item: ItemLike,
        fluidStack: FluidStack,
        count: Int = 1,
        patch: DataComponentPatch = DataComponentPatch.EMPTY,
    ): ItemStack {
        val stack: ItemStack = createItemStack(item, count, patch)
        updateFluid(stack, fluidStack)
        return stack
    }

    @JvmStatic
    fun getFluid(container: ItemStack): FluidStack = container.getOrDefault(HCDataComponents.FLUID, SimpleFluidContent.EMPTY).copy()

    @JvmStatic
    fun updateFluid(container: ItemStack, newStack: FluidStack) {
        if (newStack.isEmpty) {
            container.remove(HCDataComponents.FLUID)
        } else {
            container[HCDataComponents.FLUID] = SimpleFluidContent.copyOf(newStack)
        }
    }

    @JvmStatic
    fun addFluidTooltip(
        stack: FluidStack,
        consumer: Consumer<Text>,
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
        val fluidId: ResourceLocation = stack.fluidHolder.toLike().getId()
        if (flag.isAdvanced) {
            fluidId
                .toString()
                .toText()
                .withStyle(HTDefaultColor.GRAY)
                .let(consumer::accept)
        }
        // Mod Name
        fluidId
            .namespace
            .let(HTTextUtil::getModNameText)
            .withStyle(HTDefaultColor.BLUE)
            .withStyle(ChatFormatting.ITALIC)
            .let(consumer::accept)
    }

    @JvmStatic
    fun getFluidBarWidth(container: ItemStack): Int = when {
        container.count > 1 -> 0
        else -> (13.0 - 13.0 * getFluidDurability(container)).roundToInt()
    }

    @JvmStatic
    private fun getFluidDurability(container: ItemStack): Double {
        val bestRatio: Double = HTFluidCapabilities
            .getFirstTank(container)
            ?.getLevelAsFraction()
            ?.toDouble()
            ?: 0.0
        return 1 - bestRatio
    }

    //    Item    //
}
