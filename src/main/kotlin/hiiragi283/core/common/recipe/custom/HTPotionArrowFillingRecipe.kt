package hiiragi283.core.common.recipe.custom

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.neoforged.neoforge.fluids.FluidStack

data object HTPotionArrowFillingRecipe : HTTankFillingRecipe {
    const val FLUID_AMOUNT: Int = HTConst.DEFAULT_FLUID_AMOUNT / 8

    override fun testContainer(stack: ItemStack): Boolean = stack.`is`(Items.ARROW)

    override fun testFluid(stack: FluidStack): Boolean {
        val contents: BottledPotionContents = HTPotionHelper.getContents(stack) ?: return false
        return !contents.isEmpty && contents.bottleType == HTBottleType.LINGERING
    }

    override fun getRequiredFluidAmount(input: HTItemAndFluidRecipeInput): Int = FLUID_AMOUNT

    override fun assemble(input: HTItemAndFluidRecipeInput, preview: Boolean): ItemStack {
        val (contents: PotionContents, _) = HTPotionHelper.getContents(input.fluid) ?: return ItemStack.EMPTY
        return HTPotionHelper.createPotion(Items.TIPPED_ARROW, contents)
    }
}
