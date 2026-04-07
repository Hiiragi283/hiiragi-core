package hiiragi283.core.common.data.tank

import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.neoforged.neoforge.fluids.FluidStack

data object HTPotionArrowTankInteraction : HTTankInteraction {
    override val amount: Int = 125

    override fun canEmptyContainer(container: HTItemResourceType): Boolean = false

    override fun emptyContainer(container: HTItemResourceType): Pair<ItemStack, FluidStack> = ItemStack.EMPTY to FluidStack.EMPTY

    override fun canFillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): Boolean {
        if (!container.isOf(Items.ARROW)) return false
        val contents: BottledPotionContents = HTPotionHelper.getContents(fluidStack) ?: return false
        return !contents.isEmpty && contents.bottleType == HTBottleType.LINGERING
    }

    override fun fillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): ItemStack {
        val (contents: PotionContents, bottleType: HTBottleType) = HTPotionHelper.getContents(fluidStack) ?: return ItemStack.EMPTY
        return when {
            bottleType == HTBottleType.LINGERING -> HTPotionHelper.createPotion(Items.TIPPED_ARROW, contents)
            else -> ItemStack.EMPTY
        }
    }
}
