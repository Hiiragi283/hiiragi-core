package hiiragi283.core.common.data.tank

import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTColoringTankInteraction(val inputTag: TagKey<Item>, val color: HTFluidContent, val colored: ItemStack) :
    HTTankInteraction.Filling {
    override fun canFillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): Boolean =
        container.isOf(inputTag) && fluidStack.isOf(color)

    override fun fillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): ItemStack = colored

    override val amount: Int = 250
}
