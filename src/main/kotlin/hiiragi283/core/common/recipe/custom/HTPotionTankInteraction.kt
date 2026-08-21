package hiiragi283.core.common.recipe.custom

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.result.HTItemAndFluidResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

data object HTPotionTankInteraction {
    const val FLUID_AMOUNT: Int = FluidType.BUCKET_VOLUME / 4

    data object Emptying : HTTankEmptyingRecipe {
        override fun test(input: ItemStack): Boolean {
            val bool1: Boolean = HTBottleType.entries.any { input.`is`(it.asItem()) }
            val bool2: Boolean = HTPotionHelper.getContentsFromBottle(input) != null
            return bool1 && bool2
        }

        override fun getMatchingStack(input: ItemStack): ItemStack = when {
            test(input) -> input.copyWithCount(1)
            else -> ItemStack.EMPTY
        }

        override fun apply(input: ItemStack): HTItemAndFluidResult {
            val contents: BottledPotionContents = HTPotionHelper.getContentsFromBottle(input) ?: return HTItemAndFluidResult(FluidStack.EMPTY)
            return HTItemAndFluidResult(
                ItemStack(Items.GLASS_BOTTLE),
                contents.toFluidStack(FLUID_AMOUNT),
            )
        }

        override fun isIncomplete(): Boolean = false
    }

    data object Filling : HTTankFillingRecipe {
        override fun testContainer(stack: ItemStack): Boolean = stack.`is`(Items.GLASS_BOTTLE)

        override fun testFluid(stack: FluidStack): Boolean = HTPotionHelper.getContents(stack) != null

        override fun apply(first: ItemStack, second: FluidStack): ItemStack = HTPotionHelper
            .getContents(second)
            ?.toBottleItem()
            ?: ItemStack.EMPTY

        override fun getMatchingStacks(first: ItemStack, second: FluidStack): Pair<ItemStack, FluidStack> = first.copyWithCount(1) to second.copyWithAmount(FLUID_AMOUNT)

        override fun isIncomplete(): Boolean = false
    }
}
