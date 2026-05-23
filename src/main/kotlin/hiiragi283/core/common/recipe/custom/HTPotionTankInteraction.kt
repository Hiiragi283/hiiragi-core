package hiiragi283.core.common.recipe.custom

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.result.HTItemAndFluidResult
import hiiragi283.core.util.HCPotionFluidHelper
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

        override fun getRequiredAmount(input: ItemStack): Int = when {
            test(input) -> 1
            else -> 0
        }

        override fun assemble(input: ItemStack): HTItemAndFluidResult {
            val contents: BottledPotionContents = HTPotionHelper.getContentsFromBottle(input) ?: return HTItemAndFluidResult(FluidStack.EMPTY)
            return HTItemAndFluidResult(
                ItemStack(Items.GLASS_BOTTLE),
                HCPotionFluidHelper.createFluid(contents, FLUID_AMOUNT),
            )
        }
    }

    data object Filling : HTTankFillingRecipe {
        override fun testContainer(stack: ItemStack): Boolean = stack.`is`(Items.GLASS_BOTTLE)

        override fun testFluid(stack: FluidStack): Boolean = HTPotionHelper.getContents(stack) != null

        override fun assemble(firstInput: ItemStack, secondInput: FluidStack): ItemStack = HTPotionHelper
            .getContents(secondInput)
            ?.let(HTPotionHelper::createPotion)
            ?: ItemStack.EMPTY

        override fun getRequiredAmount(first: ItemStack, second: FluidStack): Pair<Int, Int> = 1 to FLUID_AMOUNT
    }
}
