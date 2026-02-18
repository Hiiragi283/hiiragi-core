package hiiragi283.core.common.capability

import hiiragi283.core.api.capability.HTMultiCapability
import hiiragi283.core.api.capability.tankRange
import hiiragi283.core.api.storage.fluid.HTFluidHandler
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.storage.component.HTComponentHandler
import hiiragi283.core.common.storage.fluid.HTComponentFluidHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

object HTFluidCapabilities : HTMultiCapability<IFluidHandler, IFluidHandlerItem> {
    override val block: BlockCapability<IFluidHandler, Direction?> = Capabilities.FluidHandler.BLOCK
    override val entity: EntityCapability<IFluidHandler, Direction?> = Capabilities.FluidHandler.ENTITY
    override val item: ItemCapability<IFluidHandlerItem, Void?> = Capabilities.FluidHandler.ITEM

    fun wrapHandler(handler: IFluidHandler, context: Direction?): List<HTFluidView> = when (handler) {
        is HTFluidHandler -> handler.getFluidTanks(context)

        else -> handler.tankRange.map { tank: Int ->
            object : HTFluidView {
                override fun getResource(): HTFluidResourceType? = handler.getFluidInTank(tank).toResource()

                override fun getCapacity(resource: HTFluidResourceType?): Int = handler.getTankCapacity(tank)

                override fun getAmount(): Int = handler.getFluidInTank(tank).amount
            }
        }
    }

    //    Block    //

    fun getFluidViews(level: Level, pos: BlockPos, side: Direction?): List<HTFluidView> =
        getCapability(level, pos, side)?.let { wrapHandler(it, side) } ?: emptyList()

    fun getFluidView(
        level: Level,
        pos: BlockPos,
        side: Direction?,
        tank: Int,
    ): HTFluidView? = getFluidViews(level, pos, side).getOrNull(tank)

    //    Entity    //

    fun getFluidViews(entity: Entity, side: Direction?): List<HTFluidView> =
        getCapability(entity, side)?.let { wrapHandler(it, side) } ?: emptyList()

    fun getFluidView(entity: Entity, side: Direction?, tank: Int): HTFluidView? = getFluidViews(entity, side).getOrNull(tank)

    //    Item    //

    fun getFluidViews(stack: ItemStack): List<HTFluidView> = getCapability(stack)?.let { wrapHandler(it, null) } ?: emptyList()

    fun getFluidView(stack: ItemStack, tank: Int): HTFluidView? = getFluidViews(stack).getOrNull(tank)

    // HTItemResourceType

    fun getFluidViews(resource: HTItemResourceType?): List<HTFluidView> =
        getCapability(resource)?.let { wrapHandler(it, null) } ?: emptyList()

    fun getFluidView(resource: HTItemResourceType?, tank: Int): HTFluidView? = getFluidViews(resource).getOrNull(tank)

    //    Register    //

    fun registerItem(
        event: RegisterCapabilitiesEvent,
        size: Int,
        factory: HTComponentHandler.ContainerFactory<HTFluidTank>,
        vararg items: ItemLike,
    ) {
        registerItem(
            event,
            { stack: ItemStack -> HTComponentFluidHandler(stack, size, factory) },
            *items,
        )
    }

    fun registerItem(event: RegisterCapabilitiesEvent, factory: HTComponentHandler.ContainerFactory<HTFluidTank>, vararg items: ItemLike) {
        registerItem(
            event,
            1,
            factory,
            *items,
        )
    }
}
