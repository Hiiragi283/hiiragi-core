package hiiragi283.core.util

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.capability.HTFluidCapabilities
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.fluid.insert
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.storage.resource.HTResourceSlot
import hiiragi283.core.api.storage.resource.HTResourceType
import hiiragi283.core.api.storage.resource.HTResourceView
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.redstone.Redstone
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import org.slf4j.Logger
import java.util.function.Consumer
import java.util.function.ToIntBiFunction

object HTStackSlotHelper {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @JvmStatic
    fun <RESOURCE : HTResourceType<*>, SLOT : HTResourceSlot<RESOURCE>> moveResource(
        from: SLOT?,
        to: SLOT?,
        amount: Int = from?.getAmount() ?: 0,
        access: HTStorageAccess = HTStorageAccess.INTERNAL,
    ): HTResourceMoveResult<RESOURCE> = TODO()

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
    fun calculateRedstoneLevel(amount: Int, capacity: Int): Int {
        val level: Float = when (capacity) {
            0 -> 0f
            else -> amount / capacity.toFloat()
        }
        return Mth.lerpDiscrete(level, Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)
    }

    @JvmStatic
    fun calculateRedstoneLevel(view: HTAmountView<*>): Int =
        Mth.lerpDiscrete(view.getStoredLevel().toFloat(), Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)

    //    Item    //

    @JvmStatic
    inline fun shrinkItemStack(
        slot: HTItemSlot,
        remainderGetter: (HTItemResourceType) -> ItemStack,
        stackSetter: (HTItemResourceType) -> Unit,
        amount: Int,
        action: HTStorageAction,
    ): Int {
        val stackIn: HTItemResourceType = slot.getResource() ?: return 0
        if (action.execute()) {
            stackIn
                .let(remainderGetter)
                .let(ItemStack::toResource)
                ?.let(stackSetter)
        }
        return slot.extract(amount, action, HTStorageAccess.INTERNAL)
    }

    @JvmStatic
    fun insertStacks(
        slots: Iterable<HTItemSlot>,
        stack: ItemStack,
        action: HTStorageAction,
        filter: (HTItemSlot, HTItemResourceType) -> Boolean = HTItemSlot::isValid,
        onBreak: () -> Unit = {},
    ): ItemStack {
        val remainder: ItemStack = stack.copy()
        for (slot: HTItemSlot in slots) {
            val remainderResource: HTItemResourceType = remainder.toResource() ?: break
            if (filter(slot, remainderResource)) {
                val remainderCount: Int = slot.insert(remainderResource, remainder.count, action, HTStorageAccess.INTERNAL)
                if (action.execute()) {
                    remainder.count = remainderCount
                }
                if (remainder.count <= 0) {
                    onBreak()
                    break
                }
            }
        }
        return remainder
    }

    //    Fluid    //

    /**
     * @see net.neoforged.neoforge.fluids.FluidUtil.interactWithFluidHandler
     * @see mekanism.common.util.FluidUtils.handleTankInteraction
     */
    @JvmStatic
    fun interact(
        player: Player,
        hand: InteractionHand,
        stack: ItemStack,
        tank: HTFluidTank,
    ): Boolean {
        if (!HTFluidCapabilities.hasCapability(stack)) return false
        val handler: IFluidHandlerItem = HTFluidCapabilities.getCapability(stack.copyWithCount(1)) ?: return false
        val resourceIn: HTFluidResourceType? = tank.getResource()
        val firstFluid: FluidStack = when (resourceIn == null) {
            true -> handler.drain(Int.MAX_VALUE, HTStorageAction.SIMULATE.toFluid())
            false -> handler.drain(resourceIn.toStack(Int.MAX_VALUE), HTStorageAction.SIMULATE.toFluid())
        }
        if (!firstFluid.isEmpty) {
            if (resourceIn != null) {
                val filled: Int = handler.fill(tank.getFluidStack(), HTStorageAction.of(player.isCreative).toFluid())
                val container: ItemStack = handler.container
                if (filled > 0) {
                    if (stack.count == 1) {
                        player.setItemInHand(hand, container)
                    } else if (stack.count > 1 && player.inventory.add(container)) {
                        stack.shrink(1)
                    } else {
                        player.drop(container, false, true)
                        stack.shrink(1)
                    }
                    tank.extract(filled, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)
                    return true
                }
            }
        } else {
            val remainder: FluidStack = tank.insert(firstFluid, HTStorageAction.SIMULATE, HTStorageAccess.MANUAL)
            val remainderAmount: Int = remainder.amount
            val storedAmount: Int = firstFluid.amount
            if (remainderAmount < storedAmount) {
                var filled = false
                val drained: FluidStack = handler.drain(
                    firstFluid.copyWithAmount(storedAmount - remainderAmount),
                    HTStorageAction.of(player.isCreative).toFluid(),
                )
                if (!drained.isEmpty) {
                    val container: ItemStack = handler.container
                    if (player.isCreative) {
                        filled = true
                    } else if (!container.isEmpty) {
                        if (stack.count == 1) {
                            player.setItemInHand(hand, container)
                            filled = true
                        } else if (player.inventory.add(container)) {
                            stack.shrink(1)
                            filled = true
                        }
                    } else {
                        stack.shrink(1)
                        if (stack.isEmpty) {
                            player.setItemInHand(hand, ItemStack.EMPTY)
                        }
                        filled = true
                    }
                    if (filled) {
                        tank.insert(drained, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)
                        return true
                    }
                }
            }
        }
        return false
    }

    @JvmStatic
    fun moveFluid(from: HTItemSlot, containerSetter: Consumer<ItemStack>, to: HTFluidTank): Boolean {
        val resourceType: HTItemResourceType = from.getResource() ?: return false
        if (!HTFluidCapabilities.hasCapability(resourceType)) return false
        val wrapper: HTFluidHandlerItemWrapper = HTFluidHandlerItemWrapper.create(resourceType) ?: return false
        return moveFluid(from, containerSetter, wrapper, to)
    }

    @JvmStatic
    fun moveFluid(
        slot: HTItemSlot,
        containerSetter: Consumer<ItemStack>,
        from: HTFluidHandlerItemWrapper,
        to: HTFluidTank,
    ): Boolean {
        val result: HTResourceMoveResult<HTFluidResourceType> = moveResource(from, to)
        if (result.succeeded) {
            val container: ItemStack = from.container
            if (!container.isEmpty) {
                if (container.count == 1) {
                    containerSetter.accept(container)
                } else {
                    slot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)
                }
            }
        }
        return result.succeeded
    }

    class HTFluidHandlerItemWrapper private constructor(private val handler: IFluidHandlerItem) :
        HTFluidTank,
        HTContentListener.Empty,
        HTValueSerializable.Empty {
            companion object {
                @JvmStatic
                fun create(resourceType: HTItemResourceType): HTFluidHandlerItemWrapper? =
                    HTFluidCapabilities.getCapability(resourceType)?.let(::create)

                @JvmStatic
                fun create(handler: IFluidHandlerItem): HTFluidHandlerItemWrapper? = when (handler.tanks) {
                    1 -> HTFluidHandlerItemWrapper(handler)
                    else -> null
                }
            }

            val container: ItemStack get() = handler.container

            override fun isValid(resource: HTFluidResourceType): Boolean = handler.isFluidValid(0, resource.toStack(1))

            override fun insert(
                resource: HTFluidResourceType?,
                amount: Int,
                action: HTStorageAction,
                access: HTStorageAccess,
            ): Int {
                if (resource == null || amount <= 0) return 0
                val filled: Int = handler.fill(resource.toStack(amount), action.toFluid())
                return amount - filled
            }

            override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
                handler.drain(amount, action.toFluid()).amount

            override fun getResource(): HTFluidResourceType? = handler.getFluidInTank(0).toResource()

            override fun getCapacity(resource: HTFluidResourceType?): Int = handler.getTankCapacity(0)

            override fun getAmount(): Int = handler.getFluidInTank(0).amount
        }
}
