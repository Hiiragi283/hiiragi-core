package hiiragi283.core.common.capability

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.capability.HTMultiCapability
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidHandler
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.storage.fluid.toStackOrEmpty
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.impl.storage.fluid.HTItemFluidHandler
import hiiragi283.core.impl.storage.fluid.HTItemFluidTank
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

    fun wrapAsTank(handler: IFluidHandler, context: Direction?): HTFluidTank? = when {
        handler is HTFluidHandler -> handler.getFluidTank(0, context)
        handler.tanks == 0 -> null
        else -> object : HTFluidTank, HTContentListener.Empty, HTValueSerializable.Empty {
            override fun isValid(resource: HTFluidResourceType): Boolean = handler.isFluidValid(0, resource.toStack(1))

            override fun insert(
                resource: HTFluidResourceType?,
                amount: Int,
                action: HTStorageAction,
                access: HTStorageAccess,
            ): Int {
                val filled: Int = handler.fill(resource.toStackOrEmpty(amount), action.toFluid())
                return amount - filled
            }

            override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = handler.drain(amount, action.toFluid()).amount

            override fun getResource(): HTFluidResourceType? = handler.getFluidInTank(0).toResource()

            override fun getCapacity(resource: HTFluidResourceType?): Int = handler.getTankCapacity(0)

            override fun getAmount(): Int = handler.getFluidInTank(0).amount
        }
    }

    //    Block    //

    fun getFirstTank(level: Level, pos: BlockPos, side: Direction?): HTFluidTank? = getCapability(level, pos, side)?.let { wrapAsTank(it, side) }

    //    Entity    //

    fun getFirstTank(entity: Entity, side: Direction?): HTFluidTank? = getCapability(entity, side)?.let { wrapAsTank(it, side) }

    //    Item    //

    fun getFirstTank(stack: ItemStack): HTFluidTank? = getCapability(stack)?.let { wrapAsTank(it, null) }

    fun getFirstTank(resource: HTItemResourceType?): HTFluidTank? = getCapability(resource)?.let { wrapAsTank(it, null) }

    //    Register    //

    fun registerItemTank(event: RegisterCapabilitiesEvent, factory: (ItemStack) -> HTItemFluidTank, vararg items: ItemLike) {
        event.registerItem(
            item,
            { stack: ItemStack, _ -> HTItemFluidHandler { factory(stack) } },
            *items,
        )
    }
}
