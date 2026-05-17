package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HTItemAndFluidResult(val item: ItemStack, val fluid: FluidStack) {
    constructor(item: ItemStack) : this(item, FluidStack.EMPTY)

    constructor(fluid: FluidStack) : this(ItemStack.EMPTY, fluid)

    constructor(pair: Pair<ItemStack, FluidStack>) : this(pair.first, pair.second)

    constructor(either: Either<ItemStack, FluidStack>) : this(either.leftOrNull() ?: ItemStack.EMPTY, either.getOrNull() ?: FluidStack.EMPTY)

    constructor(ior: Ior<ItemStack, FluidStack>) : this(ior.getLeft() ?: ItemStack.EMPTY, ior.getRight() ?: FluidStack.EMPTY)
}
