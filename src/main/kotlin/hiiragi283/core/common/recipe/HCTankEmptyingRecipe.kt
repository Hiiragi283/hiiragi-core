package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.ingredient.getMatchingStack
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemAndFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.codec.convert
import hiiragi283.core.api.util.Option
import hiiragi283.core.api.util.getOrElse
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

class HCTankEmptyingRecipe(val ingredient: Ingredient, val fluidResult: HTFluidResult, val itemResult: Option<HTItemResult>) :
    HTTankEmptyingRecipe,
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCTankEmptyingRecipe> = HTCodecs.recordMap { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCTankEmptyingRecipe::ingredient),
                    HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT).forGetter(HCTankEmptyingRecipe::fluidResult),
                    HTItemResult.CODEC.optionalFieldOf(HTConst.ITEM_RESULT).convert().forGetter(HCTankEmptyingRecipe::itemResult),
                ).apply(instance, ::HCTankEmptyingRecipe)
        }
    }

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getMatchingStack(input: ItemStack): ItemStack = ingredient.getMatchingStack(input)

    override fun assemble(input: ItemStack): HTItemAndFluidResult {
        val fluidStack: FluidStack = fluidResult.create()
        val itemStack: ItemStack = itemResult.map(HTItemResult::createOrEmpty).getOrElse(ItemStack::EMPTY)
        return HTItemAndFluidResult(itemStack, fluidStack)
    }

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.EMPTYING

    override fun getType(): RecipeType<*> = HCRecipeTypes.EMPTYING

    override fun isIncomplete(): Boolean = ingredient.hasNoItems() || itemResult.fold({ false }, HTItemResult::isIncomplete)
}
