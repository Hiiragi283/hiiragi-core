package hiiragi283.core.common.fluid

import hiiragi283.core.api.fluid.HTFluidType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTPotionFluidType(properties: Properties) : HTFluidType(properties) {
    override fun getDescriptionId(stack: FluidStack): String = HTPotionHelper.getPotionDescId(stack) ?: super.getDescriptionId(stack)

    override fun getBucket(stack: FluidStack): ItemStack = HTPotionHelper.getContents(stack)?.toBucketStack() ?: super.getBucket(stack)
}
