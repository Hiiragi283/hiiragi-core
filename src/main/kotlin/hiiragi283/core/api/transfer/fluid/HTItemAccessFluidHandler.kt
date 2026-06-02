package hiiragi283.core.api.transfer.fluid

import hiiragi283.core.setup.HCDataComponents
import hiiragi283.lib.fluid.toTemplateOrNull
import net.minecraft.world.item.Item
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * @see net.neoforged.neoforge.transfer.fluid.ItemAccessFluidHandler
 */
open class HTItemAccessFluidHandler(itemAccess: ItemAccess, protected val capacity: Int) : ItemAccessResourceHandler<FluidResource>(itemAccess, 1) {
    protected val validItem: Item = itemAccess.resource.item

    protected fun isValidItem(resource: ItemResource): Boolean = resource.`is`(validItem)

    override fun getResourceFrom(accessResource: ItemResource, index: Int): FluidResource = when {
        isValidItem(accessResource) -> FluidResource.of(accessResource.get(HCDataComponents.FLUID))
        else -> FluidResource.EMPTY
    }

    override fun getAmountFrom(accessResource: ItemResource, index: Int): Int = when {
        isValidItem(accessResource) -> accessResource.get(HCDataComponents.FLUID)?.amount() ?: 0
        else -> 0
    }

    override fun update(accessResource: ItemResource, index: Int, newResource: FluidResource, newAmount: Int): ItemResource = accessResource.with(HCDataComponents.FLUID, newResource.toStack(newAmount).toTemplateOrNull())

    override fun isValid(index: Int, resource: FluidResource): Boolean = isValidItem(itemAccess.resource)

    override fun getCapacity(index: Int, resource: FluidResource): Int = capacity
}
