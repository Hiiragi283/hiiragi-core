package hiiragi283.core.client

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.resource.toId
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack

data object HTPotionFluidExtensions : IClientFluidTypeExtensions {
    private val sourceTexture: ResourceLocation = HTConst.NEOFORGE.toId(HTConst.BLOCK, "milk_still")
    private val flowingTexture: ResourceLocation = HTConst.NEOFORGE.toId(HTConst.BLOCK, "milk_flowing")

    override fun getStillTexture(): ResourceLocation = sourceTexture

    override fun getFlowingTexture(): ResourceLocation = flowingTexture

    override fun getTintColor(stack: FluidStack): Int = "ff000000".hexToInt() or HTPotionHelper.getPotion(stack).color
}
