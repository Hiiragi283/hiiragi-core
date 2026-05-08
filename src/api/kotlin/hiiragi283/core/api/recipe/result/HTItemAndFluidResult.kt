package hiiragi283.core.api.recipe.result

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HTItemAndFluidResult(val item: ItemStack, val fluid: FluidStack) {
    constructor(item: ItemStack) : this(item, FluidStack.EMPTY)

    constructor(fluid: FluidStack) : this(ItemStack.EMPTY, fluid)

    constructor(either: Either<ItemStack, FluidStack>) : this(either.left().orElseGet(ItemStack::EMPTY), either.right().orElseGet(FluidStack::EMPTY))

    constructor(ior: Ior<ItemStack, FluidStack>) : this(ior.getLeft() ?: ItemStack.EMPTY, ior.getRight() ?: FluidStack.EMPTY)
}
