package hiiragi283.core.api.recipe.input

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 単一の[液体][FluidStack]を保持する[HTFluidRecipeInput]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
class HTSingleFluidRecipeInput(val fluid: FluidStack) : HTFluidRecipeInput {
    override fun getFluid(index: Int): FluidStack = fluid

    override fun getFluidSize(): Int = 1

    override fun getItem(index: Int): ItemStack = ItemStack.EMPTY

    override fun size(): Int = 0
}
