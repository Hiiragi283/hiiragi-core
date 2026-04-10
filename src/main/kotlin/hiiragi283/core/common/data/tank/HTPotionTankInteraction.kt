package hiiragi283.core.common.data.tank

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.util.HCPotionFluidHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack

data object HTPotionTankInteraction : HTTankInteraction.Emptying, HTTankInteraction.Filling {
    override val amount: Int = HTConst.DEFAULT_FLUID_AMOUNT / 4

    override fun canEmptyContainer(container: HTItemResourceType): Boolean {
        val bool1: Boolean = HTBottleType.entries.any { container.isOf(it.asItem()) }
        val bool2: Boolean = HTPotionHelper.getContentsFromBottle(container) != null
        return bool1 && bool2
    }

    override fun emptyContainer(container: HTItemResourceType): Pair<ItemStack, FluidStack> {
        val content: BottledPotionContents =
            HTPotionHelper.getContentsFromBottle(container) ?: return ItemStack.EMPTY to FluidStack.EMPTY
        return ItemStack(Items.GLASS_BOTTLE) to HCPotionFluidHelper.createFluid(content, amount)
    }

    override fun canFillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): Boolean {
        if (!container.isOf(Items.GLASS_BOTTLE)) return false
        return HTPotionHelper.getContents(fluidStack) != null
    }

    override fun fillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): ItemStack = HTPotionHelper
        .getContents(fluidStack)
        ?.let(HTPotionHelper::createPotion)
        ?: ItemStack.EMPTY
}
