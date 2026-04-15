package hiiragi283.core.common.recipe.custom

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.util.HCPotionFluidHelper
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

data object HTPotionTankInteraction {
    const val FLUID_AMOUNT: Int = HTConst.DEFAULT_FLUID_AMOUNT / 4

    data object Emptying : HTTankEmptyingRecipe {
        override fun testContainer(stack: ItemStack): Boolean {
            val bool1: Boolean = HTBottleType.entries.any { stack.`is`(it.asItem()) }
            val bool2: Boolean = HTPotionHelper.getContentsFromBottle(stack) != null
            return bool1 && bool2
        }

        override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack(Items.GLASS_BOTTLE)

        override fun assembleFluid(input: SingleRecipeInput, registries: HolderLookup.Provider): FluidStack {
            val contents: BottledPotionContents = HTPotionHelper.getContentsFromBottle(input.item()) ?: return FluidStack.EMPTY
            return HCPotionFluidHelper.createFluid(contents, FLUID_AMOUNT)
        }
    }

    data object Filling : HTTankFillingRecipe {
        override fun testContainer(stack: ItemStack): Boolean = stack.`is`(Items.GLASS_BOTTLE)

        override fun testFluid(stack: FluidStack): Boolean = HTPotionHelper.getContents(stack) != null

        override fun getRequiredFluidAmount(input: HTItemAndFluidRecipeInput): Int = FLUID_AMOUNT

        override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack = HTPotionHelper
            .getContents(input.fluid)
            ?.let(HTPotionHelper::createPotion)
            ?: ItemStack.EMPTY
    }
}
