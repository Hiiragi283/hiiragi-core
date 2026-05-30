package hiiragi283.lib.recipe.result

import hiiragi283.lib.util.Either
import hiiragi283.lib.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@ConsistentCopyVisibility
@JvmRecord
data class HTItemAndFluidResult private constructor(val item: ItemStack, val fluid: FluidStack) {
    companion object {
        @JvmField
        val EMPTY = HTItemAndFluidResult(ItemStack.EMPTY, FluidStack.EMPTY)

        @JvmStatic
        fun create(item: ItemStack, fluid: FluidStack): HTItemAndFluidResult = when {
            item.isEmpty && fluid.isEmpty -> EMPTY
            else -> HTItemAndFluidResult(item, fluid)
        }

        @JvmStatic
        fun create(item: ItemStack): HTItemAndFluidResult = create(item, FluidStack.EMPTY)

        @JvmStatic
        fun create(fluid: FluidStack): HTItemAndFluidResult = create(ItemStack.EMPTY, fluid)

        @JvmStatic
        fun create(pair: Pair<ItemStack, FluidStack>): HTItemAndFluidResult = create(pair.first, pair.second)

        @JvmStatic
        fun create(either: Either<ItemStack, FluidStack>): HTItemAndFluidResult = either.fold(::create, ::create)

        @JvmStatic
        fun create(ior: Ior<ItemStack, FluidStack>): HTItemAndFluidResult = ior.fold(::create, ::create, ::create)
    }

    fun isEmpty(): Boolean = this == EMPTY || item.isEmpty && fluid.isEmpty
}
