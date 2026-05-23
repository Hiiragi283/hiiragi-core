package hiiragi283.core.common.fluid

import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.lib.fluid.HTFluidType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.text.Text
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTPotionFluidType(properties: Properties) : HTFluidType(properties) {
    override fun getDescription(stack: FluidStack): Text = HTPotionHelper.getContents(stack)?.getText() ?: super.getDescription()

    override fun getBucket(stack: FluidStack): ItemStack = HTPotionHelper.getContents(stack)?.let(HCPotionFluidHelper::createBucket) ?: super.getBucket(stack)
}
