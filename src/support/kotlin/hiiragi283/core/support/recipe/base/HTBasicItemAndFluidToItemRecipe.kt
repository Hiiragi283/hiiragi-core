package hiiragi283.core.support.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.progress.HTBiProgressProvider
import hiiragi283.core.api.recipe.progress.HTProgressData
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.support.data.recipe.HTItemAndFluidToItemRecipeBuilder
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

open class HTBasicItemAndFluidToItemRecipe(
    val itemIngredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    val consumeItem: Boolean,
    val result: HTItemResult,
    override val progressData: HTProgressData,
) : HTItemAndFluidToItemRecipe,
    HTBiProgressProvider.Simple<ItemStack, FluidStack> {
    companion object {
        @JvmStatic
        fun <T : HTBasicItemAndFluidToItemRecipe> codec(factory: HTItemAndFluidToItemRecipeBuilder.Factory<T>): MapCodec<T> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HTBasicItemAndFluidToItemRecipe::itemIngredient),
                    HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HTBasicItemAndFluidToItemRecipe::fluidIngredient),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTBasicItemAndFluidToItemRecipe::result),
                    HTProgressData.CODEC.forGetter(HTBasicItemAndFluidToItemRecipe::progressData),
                ).apply(instance, factory::create)
        }
    }

    override fun test(first: ItemStack, second: FluidStack): Boolean = itemIngredient.test(first) && fluidIngredient.test(second)

    override fun getMatchingStacks(first: ItemStack, second: FluidStack): Pair<ItemStack, FluidStack> = Pair(
        when (consumeItem) {
            true -> itemIngredient.getMatchingStack(first)
            false -> ItemStack.EMPTY
        },
        fluidIngredient.getMatchingStack(second),
    )

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): ItemStack = result.createOrEmpty()

    override fun isIncomplete(): Boolean = itemIngredient.isIncomplete() || fluidIngredient.isIncomplete() || result.isIncomplete()
}
