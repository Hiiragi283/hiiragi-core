package hiiragi283.core.common.item

import hiiragi283.core.api.item.HTPotionBasedItem
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.text.Text
import hiiragi283.core.common.util.HCPotionFluidHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.fluid.BucketResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.Objects

class HTPotionBucketItem(content: Fluid, properties: Properties) : HTPotionBasedItem(properties) {
    override fun getName(stack: ItemStack): Text = HTPotionHelper.getPotionText(stack) ?: super.getName(stack)

    /**
     * @see BucketResourceHandler
     */
    class BucketHandler(itemAccess: ItemAccess) : ItemAccessResourceHandler<FluidResource>(itemAccess, 1) {
        override fun getResourceFrom(accessResource: ItemResource, index: Int): FluidResource =
            HTPotionHelper.getContents(accessResource)?.let(HCPotionFluidHelper::createResource) ?: FluidResource.EMPTY

        override fun getAmountFrom(accessResource: ItemResource, index: Int): Int {
            val resource: FluidResource = getResourceFrom(accessResource, index)
            return when {
                resource.isEmpty -> 0
                else -> FluidType.BUCKET_VOLUME
            }
        }

        override fun update(
            accessResource: ItemResource,
            index: Int,
            newResource: FluidResource,
            newAmount: Int,
        ): ItemResource {
            if (newAmount == 0) {
                return ItemResource.of(Items.BUCKET)
            } else if (newAmount != FluidType.BUCKET_VOLUME) {
                return ItemResource.EMPTY
            } else {
                val newStack: FluidStack = newResource.toStack(newAmount)
                return newStack.fluidType.getBucket(newStack).let(ItemResource::of)
            }
        }

        override fun getCapacity(index: Int, resource: FluidResource): Int {
            Objects.checkIndex(index, size)
            return FluidType.BUCKET_VOLUME
        }
    }
}
