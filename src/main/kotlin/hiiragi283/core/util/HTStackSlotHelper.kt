package hiiragi283.core.util

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.fixedFraction
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.toResourcePair
import hiiragi283.core.api.storage.resource.HTResourceSlot
import hiiragi283.core.api.storage.resource.HTResourceType
import hiiragi283.core.api.storage.resource.HTResourceView
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.redstone.Redstone
import java.util.function.ToIntBiFunction

object HTStackSlotHelper {
    @JvmStatic
    fun <RESOURCE : HTResourceType<*>, SLOT : HTResourceSlot<RESOURCE>> moveResource(
        from: SLOT?,
        to: SLOT?,
        amount: Int = from?.getAmount() ?: 0,
        access: HTStorageAccess = HTStorageAccess.INTERNAL,
    ): HTResourceMoveResult<RESOURCE> {
        if (from == null || to == null || amount <= 0) return HTResourceMoveResult.failed()
        val resource: RESOURCE? = from.getResource()
        val simulatedExtract: Int = from.extract(amount, HTStorageAction.SIMULATE, access)
        val simulatedRemain: Int = to.insert(resource, simulatedExtract, HTStorageAction.SIMULATE, access)
        val simulatedAccepted: Int = amount - simulatedRemain
        if (simulatedAccepted == 0) return HTResourceMoveResult.failed()
        val extracted: Int = from.extract(simulatedAccepted, HTStorageAction.EXECUTE, access)
        val remainder: Int = to.insert(resource, extracted, HTStorageAction.EXECUTE, access)
        if (remainder > 0) {
            val leftover: Int = from.insert(resource, remainder, HTStorageAction.EXECUTE, access)
            if (leftover > 0) {
                HiiragiCoreAPI.LOGGER.error("Stack slot $from did not accept leftover stack from $to! Voiding it.")
            }
            return HTResourceMoveResult.succeeded(resource, remainder)
        } else {
            return HTResourceMoveResult.succeeded(null, 0)
        }
    }

    @JvmStatic
    fun <RESOURCE : HTResourceType<*>> shrinkStack(
        slot: HTResourceSlot<RESOURCE>,
        ingredient: ToIntBiFunction<RESOURCE, Int>,
        action: HTStorageAction,
    ): Int {
        val stackIn: RESOURCE = slot.getResource() ?: return 0
        return slot.extract(ingredient.applyAsInt(stackIn, slot.getAmount()), action, HTStorageAccess.INTERNAL)
    }

    @JvmStatic
    fun <RESOURCE : HTResourceType<*>> canShrinkStack(slot: HTResourceSlot<RESOURCE>, amount: Int, exactMatch: Boolean): Boolean {
        val extracted: Int = slot.extract(amount, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        return when (exactMatch) {
            true -> extracted == amount
            false -> extracted > 0
        }
    }

    @JvmStatic
    fun <RESOURCE : HTResourceType<*>> canShrinkStack(
        slot: HTResourceSlot<RESOURCE>,
        ingredient: ToIntBiFunction<RESOURCE, Int>,
        exactMatch: Boolean,
    ): Boolean {
        val amount: Int = slot.getResource()?.let { ingredient.applyAsInt(it, slot.getAmount()) } ?: return false
        val extracted: Int = slot.extract(amount, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        return when (exactMatch) {
            true -> extracted == amount
            false -> extracted > 0
        }
    }

    /**
     * 指定した[resource]をすべてのスロットへ搬入します。
     * @return 搬入されない量
     */
    @JvmStatic
    fun <RESOURCE : HTResourceType<*>> insert(
        slots: Iterable<HTResourceSlot<RESOURCE>>,
        resource: RESOURCE?,
        amount: Int,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int {
        if (resource == null || amount <= 0) return amount
        var remainder: Int = amount
        for (slot: HTResourceSlot<RESOURCE> in slots) {
            remainder = slot.insert(resource, remainder, action, access)
            if (remainder <= 0) break
        }
        return remainder
    }

    /**
     * 指定した[resource]をすべてのスロットから搬出します。
     * @return 搬出される量
     */
    @JvmStatic
    fun <RESOURCE : HTResourceType<*>> extract(
        slots: List<HTResourceSlot<RESOURCE>>,
        resource: RESOURCE?,
        amount: Int,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int {
        if (resource == null || amount <= 0) return 0

        var extracted = 0
        for (slot: HTResourceSlot<RESOURCE> in slots) {
            extracted += slot.extract(resource, amount - extracted, action, access)
            if (extracted == amount) break
        }
        return extracted
    }

    //    Amount    //

    /**
     * @see net.neoforged.neoforge.items.ItemHandlerHelper.calcRedstoneFromInventory
     * @see mekanism.common.util.MekanismUtils.redstoneLevelFromContents
     */
    @JvmStatic
    fun <RESOURCE : HTResourceType<*>> calculateRedstoneLevel(views: Iterable<HTResourceView<RESOURCE>>): Int {
        var amountSum = 0
        var capacitySum = 0
        for (view: HTResourceView<RESOURCE> in views) {
            amountSum += view.getAmount()
            capacitySum += view.getCapacity(view.getResource())
        }
        return calculateRedstoneLevel(amountSum, capacitySum)
    }

    /**
     * @see mekanism.common.util.MekanismUtils.redstoneLevelFromContents
     */
    @JvmStatic
    fun calculateRedstoneLevel(amount: Int, capacity: Int): Int =
        Mth.lerpDiscrete(fixedFraction(amount, capacity).toFloat(), Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)

    @JvmStatic
    fun calculateRedstoneLevel(view: HTAmountView): Int =
        Mth.lerpDiscrete(view.getLevelAsFraction().toFloat(), Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)

    //    Item    //

    @JvmStatic
    inline fun shrinkItemStack(
        slot: HTItemSlot,
        remainderGetter: (HTItemResourceType) -> ItemStack,
        stackSetter: (ItemStack) -> Unit,
        amount: Int,
        action: HTStorageAction,
    ): Int {
        val stackIn: HTItemResourceType = slot.getResource() ?: return 0
        if (action.execute()) {
            stackIn
                .let(remainderGetter)
                .let(stackSetter)
        }
        return slot.extract(amount, action, HTStorageAccess.INTERNAL)
    }

    @JvmStatic
    fun insertStacks(
        slot: Iterable<HTItemSlot>,
        stack: ItemStack,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int {
        val (resource: HTItemResourceType, amount: Int) = stack.toResourcePair() ?: return 0
        return insert(slot, resource, amount, action, access)
    }
}
