package hiiragi283.core.common.transfer.fluid

import hiiragi283.core.setup.HCDataComponents
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.function.ToIntFunction

class HTItemAccessFluidHandler(itemAccess: ItemAccess, private val capacityFactory: ToIntFunction<ItemResource>) :
    ItemAccessFluidHandler(itemAccess, HCDataComponents.FLUID, capacityFactory.applyAsInt(itemAccess.resource)) {
    constructor(itemAccess: ItemAccess, capacity: Int) : this(itemAccess, { capacity })

    override fun getCapacity(index: Int, resource: FluidResource): Int = capacityFactory.applyAsInt(this.itemAccess.resource)
}
