package hiiragi283.core.common.recipe

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.getMatchingStack
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemAndFluidResult
import hiiragi283.core.util.HCPotionFluidHelper
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

class VanillaBrewingRecipe(val potionFrom: Holder<Potion>, val ingredient: Ingredient, val potionTo: Holder<Potion>) : HTItemOrFluidRecipe {
    constructor(accessor: PotionBrewing.Mix<Potion>) : this(accessor.from, accessor.ingredient, accessor.to)

    private fun hasPotion(fluid: FluidStack): Boolean = HTPotionHelper.getContents(fluid)?.potion == potionFrom && fluid.amount >= FluidType.BUCKET_VOLUME

    override fun test(first: ItemStack, second: FluidStack): Boolean = ingredient.test(first) && hasPotion(second)

    override fun getMatchingStacks(first: ItemStack, second: FluidStack): Pair<ItemStack, FluidStack> = Pair(
        ingredient.getMatchingStack(first),
        when {
            hasPotion(second) -> second.copyWithAmount(FluidType.BUCKET_VOLUME)
            else -> FluidStack.EMPTY
        },
    )

    override fun isIncomplete(): Boolean = ingredient.hasNoItems()

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): HTItemAndFluidResult = BottledPotionContents(potionTo).let(HCPotionFluidHelper::createFluid).let(::HTItemAndFluidResult)

    override fun getProgressData(input: HTItemAndFluidRecipeInput): HTProgressData = HTProgressData.Time(200)
}
