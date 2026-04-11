package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTColoredContents
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.recipe.base.FluidAmount
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.ItemAmount
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.util.Ior
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.HolderLookup
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

@JvmRecord
data class HCColoringRecipe(val inputTag: TagKey<Item>, val contents: HTColoredContents<out HTHolderLike<out ItemLike, *>>) :
    HTItemOrFluidRecipe {
    override fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>> = Ior.Both(
        Predicate { stack: ItemStack -> stack.`is`(inputTag) },
        Predicate { stack: FluidStack -> contents.any { (color: HTDefaultColor, _) -> HCFluids.DyeContents[color].isOf(stack) } },
    )

    override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<ItemAmount, FluidAmount> = Ior.Right(250)

    override val time: Int
        get() = 100

    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack {
        for ((color: HTDefaultColor, item: HTHolderLike<out ItemLike, *>) in contents) {
            if (HCFluids.DyeContents[color].isOf(input.fluid)) {
                return ItemStack(item.get())
            }
        }
        return ItemStack.EMPTY
    }

    override fun assembleFluid(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): FluidStack = FluidStack.EMPTY
}
