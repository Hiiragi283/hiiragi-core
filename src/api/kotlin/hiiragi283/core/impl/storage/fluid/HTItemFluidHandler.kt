package hiiragi283.core.impl.storage.fluid

import hiiragi283.core.api.storage.fluid.HTFluidHandler
import hiiragi283.core.api.storage.fluid.HTFluidTank
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

/**
 * [HTItemFluidTank]向けの[HTFluidHandler]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
fun interface HTItemFluidHandler :
    HTFluidHandler,
    IFluidHandlerItem {
    fun getItemFluidTank(): HTItemFluidTank?

    override fun getFluidTanks(side: Direction?): List<HTFluidTank> = listOfNotNull(getItemFluidTank())

    override fun getContainer(): ItemStack = getItemFluidTank()?.container ?: ItemStack.EMPTY
}
