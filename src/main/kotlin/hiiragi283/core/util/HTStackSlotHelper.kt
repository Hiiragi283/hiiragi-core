package hiiragi283.core.util

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.toResourcePair
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.toResourcePair
import hiiragi283.core.api.storage.resource.HTResourceSlot
import hiiragi283.core.api.storage.resource.HTResourceType
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

data object HTStackSlotHelper {
    @JvmStatic
    fun <RESOURCE : HTResourceType> moveResource(
        from: HTResourceSlot<RESOURCE>?,
        to: HTResourceSlot<RESOURCE>?,
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

    /**
     * 指定した[resource]をすべてのスロットへ搬入します。
     * @return 搬入されない量
     */
    @JvmStatic
    fun <RESOURCE : HTResourceType> insert(
        slots: Sequence<HTResourceSlot<RESOURCE>>,
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
    fun <RESOURCE : HTResourceType> extract(
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

    //    Item    //

    @JvmStatic
    fun insertStacks(
        slots: Sequence<HTItemSlot>,
        stack: ItemStack,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int {
        val (resource: HTItemResourceType, amount: Int) = stack.toResourcePair() ?: return 0
        return insert(slots, resource, amount, action, access)
    }

    //    Fluid    //

    @JvmStatic
    fun insertStacks(
        tanks: Sequence<HTFluidTank>,
        stack: FluidStack,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int {
        val (resource: HTFluidResourceType, amount: Int) = stack.toResourcePair() ?: return 0
        return insert(tanks, resource, amount, action, access)
    }
}
