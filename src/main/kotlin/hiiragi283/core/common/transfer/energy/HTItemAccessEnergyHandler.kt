package hiiragi283.core.common.transfer.energy

import hiiragi283.core.setup.HCDataComponents
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.function.ToIntFunction

class HTItemAccessEnergyHandler(
    itemAccess: ItemAccess,
    private val capacityFactory: ToIntFunction<ItemResource>,
    maxInsert: Int,
    maxExtract: Int,
) : ItemAccessEnergyHandler(itemAccess, HCDataComponents.ENERGY, capacityFactory.applyAsInt(itemAccess.resource), maxInsert, maxExtract) {
    constructor(
        itemAccess: ItemAccess,
        capacityFactory: ToIntFunction<ItemResource>,
    ) : this(itemAccess, capacityFactory, capacityFactory.applyAsInt(itemAccess.resource), capacityFactory.applyAsInt(itemAccess.resource))

    constructor(
        itemAccess: ItemAccess,
        capacity: Int,
        maxInsert: Int = capacity,
        maxExtract: Int = capacity,
    ) : this(itemAccess, { capacity }, maxInsert, maxExtract)

    override fun getCapacityAsLong(): Long {
        val resource: ItemResource = itemAccess.resource
        return when {
            !resource.`is`(validItem) -> 0
            else -> itemAccess.amount * capacityFactory.applyAsInt(resource).toLong()
        }
    }
}
