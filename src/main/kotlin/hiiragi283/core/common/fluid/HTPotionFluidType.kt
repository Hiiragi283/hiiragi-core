package hiiragi283.core.common.fluid

import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.common.util.HCPotionFluidHelper
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

class HTPotionFluidType(properties: Properties) : FluidType(properties) {
    override fun getDescription(stack: FluidStack): Component = HTPotionHelper.getPotionText(stack) ?: super.getDescription(stack)

    override fun getBucket(stack: FluidStack): ItemStack =
        HTPotionHelper.getContents(stack)?.let(HCPotionFluidHelper::createBucket) ?: super.getBucket(stack)
}
